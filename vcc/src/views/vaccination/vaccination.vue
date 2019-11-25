<!--接种页面-->
<template>
    <div class="container">
        <div class="containerBody">
            <div class="title">
            <div class="titleLeft" @click="matchInfo">
                接种人信息
            </div>
            <div class="titleRight">
                疫苗信息
            </div>
        </div>
        <div class="content">
            <div class="left">
                <div class="leftTop">
                    <div class="leftTopLeft">
                        <span class="code">{{vaccinationData? vaccinationData.code:''}}号</span><span class="name">{{vaccinationData?vaccinationData.name: ''}}</span>
                    </div>
                    <div class="leftTopRight">
                        <div class="age">
                            年龄:
                        </div>
                        <div class="ageCount">
                            {{vaccinationData?vaccinationData.age:''}}周岁
                        </div>
                    </div>
                    <div class="leftTopRightTwo">
                        <div class="age">
                            性别:
                        </div>
                        <div class="ageCount">
                            {{vaccinationData?vaccinationData.sex:''}}
                        </div>
                    </div>
                </div>
                <div class="leftBottom" v-if="vaccinationData">
                    <div class="personInf">
                        <div>
                            <div v-if="status">
                                <span v-if="!ready" class="tipInf" style="color:#F42954">信息不匹配</span><img src="/static/img/error.png" class="succeed" v-if="!status">
                                <span v-if="ready" class="tipInf" style="color:#1AA95E">{{confirm?'已接种':'信息匹配'}}</span><img src="/static/img/succeed.png" class="succeed" v-if="status">
                            </div>
                            
                            <Checkbox v-model="single" class="personInf-check">{{vaccineName}}</Checkbox>
                        </div>
                        <div class="producter">
                            生产厂家：北京科兴生物制品有限公司
                        </div>
                        <div class="vaccine-info">
                            <div class="vaccine-info-part">
                                <div>接种部位：</div>
                                <Select v-model="partVal" style="width:9rem;height:2.25rem">
                                    <Option v-for="item in part" :value="item.value" :key="item.key">{{ item.value }}</Option>
                                </Select>
                            </div>
                            <div class="vaccine-info-num">
                                接种剂量：1支
                            </div>
                        </div>
                        <div class="vaccine-btnBox">
                            <!-- <button>取消接种</button> -->
                            <button v-if="single && status && ready" @click="confirmVaccine()" :disabled="confirm">确认接种</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="vaccineInf" v-if="showScan">
                    <div class="vaccineInf-top">
                        <div><span>{{vaccine.name}}</span></div>
                        <div>监管码：<span>Y750230-64368</span></div>
                        <div class="mt24">批次号：<span>Y750230-64368</span></div>
                        <div class="mt24">疫苗剂量：<span>0.5ml/支</span></div>
                    </div>
                    <div class="vaccineInf-bottom">有效期：<span>2019-09-18 12:30:28   至  2022-12-29 12:20:19</span></div>
                    <div class="vaccineInf-bottom">生产厂家：<span>{{vaccine.producer}}</span></div>
                    <div class="vaccineInf-bottom">接种份数：<span>1</span></div>
                </div>
            </div>
        </div>
        <div class="bottom-btn">
            <button @click="()=>{this.$emit('changeMenu',0);this.$router.push('/main');}">返回主页</button>
            <button @click="queryNextQueue()">下一位</button>
        </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import moment from 'moment';
    import uuid from 'uuid/v1';

    export default {
        data() {
            return {
                progress:0,
                batchId: uuid(),
                commonData: null,
                audio:null,
                confirmAudio:null,
                wrongAudio:null,
                queryVaccine: null,  //查询的本地疫苗信息
                vaccinationData: {
                    /*'customer':{
                        'code': '089',
                        'name': '李义',
                        'age': 4,
                        'sex':'女'
                    },
                    'vaccine':[
                        {
                            name:'乙肝疫苗',
                            product:'长春生物制药',
                            num:'1',
                            status:true
                        },{
                            name:'流感疫苗',
                            product:'长春生物制药',
                            num:'1',
                            status:true
                        },{
                            name:'腮腺炎疫苗',
                            product:'长春生物制药',
                            num:'1',
                            status: false
                        }

                    ]*/
                }, //接种信息
                vaccine: [
                    
                ],//疫苗扫码信息
                part: [
                    {
                        value: '左上臂',
                        key: '左上臂'
                    },
                    {
                        value: '右上臂',
                        key: '右上臂'
                    }
                ],
                partVal:'',
                single: true,
                status:false,
                vaccineName:'',
                confirm:false,
                ready:false,
                showScan:false,
                drawer:{}
            };
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        methods: {
            //比对疫苗信息
            async matchInfo(){
                //接收疫苗
                this.$device.subscribe('SCANNER_RESULT', async (data) => {
                    if(this.$route.path != '/vaccination/vaccination'){
                        return false;
                    }
                    console.log("这里是扫码枪的内容 result:" + JSON.stringify(data))
                    await this.getVaccine(data.data);//
                    let now=moment().isBefore(this.vaccine.expiry);
                    console.log("这里是日期比较结果："+JSON.stringify(now));
                    this.status=true;
                    this.showScan=true;
                    //疫苗信息与扫码的疫苗比对,根据不同结果显示不同提示信息
                    if(this.vaccine.name == this.vaccineName && now){
                        this.progress = 1;
                        this.ready=true;
                        this.confirmAudio.play();
                    }else{
                        this.progress = 2;
                        this.ready=false;
                        this.wrongAudio.play();
                    }
                });
            },
            async confirmVaccine(){
                //排队状态完成
                await this.modifyQueue({id:this.vaccinationData._id,status:0}).then(res=>{
                    console.log("这里是queue信息修改完成后的结果"+JSON.stringify(res))
                });
                this.confirm=true;
                //比对成功，疫苗数量减少1、增加出库信息、保存接种信息
                //疫苗数量减少1
                this.modifyVaccineNum({
                    id: this.queryVaccine._id,
                    total: 0,
                    surplus: -1
                });
                console.log("这里是drawer的信息:"+JSON.stringify(this.drawer))
                // //增加出库信息
                this.saveInout({
                    batchId: this.batchId,
                    ...this.commonData,
                    x: this.drawer.x,
                    y: this.drawer.y,
                    code: this.drawer.vaccine.code,
                    name: this.drawer.vaccine.name,
                    total: this.drawer.vaccine.total,
                    surplus: this.drawer.vaccine.surplus
                }); 
                //保存接种信息
                this.saveVaccination({
                    user:this.user._id,
                    device:this.device._id,
                    deviceType:this.device.type,
                    unitCode:this.device.unitCode,
                    unitName:this.device.unitName,
                    sort:'',//排队序号(叫号码)
                    customer:{          //顾客
                        code: this.vaccinationData.code,   //接种序号
                        name: this.vaccinationData.name,   //姓名
                        age: this.vaccinationData.age,    //年龄
                        vaccineCode: this.vaccinationData.vaccine.code,//疫苗编号
                        vaccineName: this.vaccinationData.vaccine.name,//疫苗名称
                        vaccineNum: this.vaccinationData.vaccine.count //疫苗数量
                    },
                    match:{                 //匹配信息(政采云响应结果)
                        vaccineCode: this.vaccinationData.vaccine.code,//疫苗编号
                        vaccineName: this.vaccinationData.vaccine.name,//疫苗名称
                        supervisionCode: this.vaccine.supervisionCode,//药品监管码
                        expiry: this.vaccine.expiry,       //有效日期
                        producer: this.vaccine.producer    //生产商
                    }
                }); //todo
            },
            getExpiryDate(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            async getVaccine(data){
                await this.$api.get('/zcy/queryVaccine',{code:data}).then(res=>{
                    this.vaccine=res.data;
                });
            },
            async modifyVaccineNum(params){
                await this.$api.post("/vaccine/modifyVaccineNum", params);
            },
            async saveInout(params){
                await this.$api.post("/inout/saveInout", params);
            },
            async saveVaccination(params){
                await this.$api.post("/vaccination/saveVaccination", params);
            },
            async queryVaccineByCondition(params){
                let res = await this.$api.get(`/vaccine/queryVaccineByCondition`, params);
                this.queryVaccine = res.data[0];
            },
            async queryDrawerByCondition(params){
                return await this.$api.get(`/drawer/queryDrawerByCondition`, params);
            },
            async modifyQueue(params){
                await this.$api.post('/queue/modifyQueue',params);
            },
            async openDrawer(obj){
                //查询疫苗所在抽屉信息,使用规则：多个柜子满足,按最少优先使用
                await this.queryVaccineByCondition({
                    device: this.device._id,
                    sortSurplus: true,
                    surplusIsNotZero: true,
                    code: obj.vaccine.code //接种信息中的疫苗code todo
                });
                let drawer = await this.queryDrawerByCondition({
                    device: this.device._id,
                    vaccineCode: this.queryVaccine._id
                });
                this.drawer=drawer.data[0];
                let vacc={};
                for(let i=0;i<this.drawer.vaccine.length;i++){
                    if(this.vaccineName==this.drawer.vaccine[i].name){
                        vacc=this.drawer.vaccine[i];
                    }
                }
                this.drawer.vaccine=vacc;
                console.log('drawer===>'+JSON.stringify(this.drawer))
                //调用打开抽屉接口
                this.$device.openDrawer({num:this.drawer.x+"#"+this.drawer.y});
                this.audio.play();
            },
            //接收接种状态
            // receiveVaccinationStatus(){
            //     this.$device.subscribe('SOCKET_VACCINATION_STATUS_DATA', (data) => {
            //         console.log('SOCKET_VACCINATION_STATUS_DATA====> result:'+JSON.stringify(data));
            //         //根据状态，判断是否跳转到首页
            //         let res=JSON.parse(data.data)
            //         if(res.status=='finish'){
            //             this.$router.push('/main');
            //         }                    
            //     });
            // },
            async queryQueue(){
                await this.$api.get('/queue/queryQueueByCondition',{status:1}).then(res=>{
                    this.vaccinationData=res.data[0];
                    console.log(this.vaccinationData);
                    this.vaccineName=res.data[0].vaccine.name;
                });
                this.openDrawer(this.vaccinationData);
            },
            async queryNextQueue(){
                if(this.confirm){
                    this.confirm=false; 
                }else{
                    //未接种点击下一位直接完成
                    await this.modifyQueue({id:this.vaccinationData._id,status:0}).then(res=>{
                        console.log("这里是queue信息修改完成后的结果"+JSON.stringify(res))
                    });
                }
                this.ready=false;
                this.status=false;
                this.showScan=false;
                await this.$api.get('/queue/queryQueueByCondition',{status:1,next:1}).then(res=>{
                    this.vaccinationData=res.data[0];
                    console.log(this.vaccinationData);
                    this.vaccineName=res.data[0].vaccine.name;
                });
                this.openDrawer(this.vaccinationData);
            }
        },
        mounted() {
            this.commonData = {
                type: 0, //0:接种
                user: this.user._id,
                device: this.device._id,
                deviceType: 1, //1:接种柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
            this.audio=new Audio();
            this.audio.src='/static/audio/outVaccine.mp3';
            this.confirmAudio=new Audio();
            this.confirmAudio.src='/static/audio/vaccineCheckConfirm.mp3';
            this.wrongAudio=new Audio();
            this.wrongAudio.src='/static/audio/vaccineCheckWrong.mp3';
            //接收推送的接种信息(home.vue中接收)
            // this.vaccinationData = this.$route.query.vaccination;
            //获取排队数据
            this.queryQueue();
            //this.receiveVaccinationStatus();
            //接收疫苗扫描后结果，与接种信息比对
            this.matchInfo();
        }
    };
</script>

<style lang="less" scoped>
    @import "~@/style/vaccination.less";
</style>