<template>
    <div class="main-table">
        <!--用户列表-->
        <Row>
            <Col offset="9" span="5" class="main-table-search">
                <div class="main-table-search-lab">名称:</div>                    
                <input  v-model="name" placeholder="请输入中文名称"  @keyup.enter="queryParameter()"></input>   
            </Col>
            <Col span="6" class="main-table-search">
                <div style="width:88px; font-size:15px">使用名称:</div>                    
                <input  v-model="key" placeholder="请输入英文名称"  @keyup.enter="queryParameter()"></input>   
            </Col>
            <Col span="4" style="display:flex">
                <Button type="primary" class="search_btn" icon="ios-search" @click="queryParameter()">查询</Button>
                <Button type="primary" class="search_btn" icon="ios-add" @click="showAddUserWin">新增</Button>
            </Col>
        </Row>
        <Table :columns="cols" :data="lists" size="small" class="table-mt" stripe border></Table>
        <Row>
            <Page :current="page" :page-size="size" :total="total"
                  @on-change="changePage" @on-page-size-change="changePageSize" show-total show-elevator show-sizer></Page>
        </Row>
        <!--编辑用户-->
        <Modal v-model="editModalWin" title="编辑用户信息" :footerHide="true" width="400" height="150" :closable="false">
            <Form ref="frmEdit" :model="formParameter" :rules="parameterValidate" :label-width="83" >
                <div>
                    <FormItem label="名称:" prop="name">
                        <Input v-model="formParameter.name" placeholder="请输入名称" style="width: 260px;"></Input>
                    </FormItem>
                </div>
                <FormItem label="使用名称:" prop="key">
                    <Input v-model="formParameter.key" placeholder="请输入使用名称" style="width: 260px;"></Input>
                </FormItem>
                <FormItem label="参数值:" prop="value">
                    <Input v-model="formParameter.value" placeholder="请输入参数值" style="width: 260px;"></Input>
                </FormItem>
                <FormItem label="说明" prop="notes">
                    <Input v-model="formParameter.notes" :autosize="{minRows: 2,maxRows: 5}" type="textarea" placeholder="请输入..." :maxlength="11" style="width: 260px;"></Input>
                </FormItem>
                <FormItem label="状态" prop="isActive">
                    <Select v-model="formParameter.isActive" clearable style="width:260px">
                        <Option v-for="item in select_type" :value="item.value" :key="item.key">{{ item.value }}</Option>
                    </Select>
                    
                </FormItem>
                <div align="right">
                    <FormItem style="margin-top: 15px;">
                        <Button  @click="cancelModel('frmEdit')">取消</Button>
                        <Button type="primary" @click="saveUser('frmEdit')">保存</Button>
                    </FormItem>
                </div>
            </Form>
        </Modal>
         <!-- 分页 -->       
        <!-- <Row>
            <Page :total="total" show-sizer show-total show-elevator @on-page-size-change="pageSizeChange" :current="search_type?search_active:active" @on-change="indexChange" :page-size="10"/>
        </Row>   -->
    </div>
</template>
<script>
    import Button from 'iview/src/components/button'
    import {mapGetters} from 'vuex'
    import moment from 'moment'
    export default{
        data(){
            return {
                total: 0,
                page: 1,
                size: 10,
                name:'',
                key:'',
                editModalWin: false,
                roleDatas:[],
                lists: [],
                select_type:[
                    {value:'true',key:1},
                    {value:'false',key:2}
                ],
                cols: [
                    {
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },{
                        title: '名称',
                        key: 'name',
                        align: 'center'
                    },{
                        title: '使用名称',
                        key: 'key',
                        align: 'center'
                    },{
                        title: '参数值',
                        key: 'value',
                        align: 'center'
                    },{
                        title: '说明',
                        key: 'notes',
                        align: 'center'
                    },{
                        title: '状态',
                        key: 'isActive',
                        align: 'center',
                        render:(h,params)=>{
                            return h(
                                'div',
                                params.row.isActive==true?'启用状态':'停用状态'
                            )
                        }
                    },{
                        title: '操作',
                        key: 'action',
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'default',
                                        size: 'small',
                                        disabled: (params.row.code == 'admin') ? true:false
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.showEditUserWin(params.row)
                                        }
                                    }
                                }, '编辑'),
                                h('Button', {
                                    props: {
                                        type: 'error',
                                        size: 'small',
                                        disabled: (params.row.code == 'admin') ? true:false
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.removeParameter(params.row)
                                        }
                                    }
                                }, '删除'),
                                h('Button', {
                                    props: {
                                        type: 'info',
                                        size: 'small',
                                        disabled: (params.row.status == 1) ? true:false
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.displayParameter(params.row)
                                        }
                                    }
                                }, params.row.isActive==true?'停用':'启用')
                            ]);
                        }
                    }
                ],
                formParameter: {
                    id:'',
                    name: '',
                    key: '',
                    value: '',
                    notes: '',
                    isActive: ''
                },
                parameterValidate: {
                    name : [{
                        required: true, message: '名称不能为空', trigger: 'blur'
                    }],
                    key: [{
                        required: true, message: '使用名称不能为空', trigger: 'blur'
                    },{
                        type: 'string', max: 20, message: '用户名超长',trigger: 'blur'
                    }],
                    value: [{
                        required: true, message: '参数值不能为空', trigger: 'blur'
                    }],
                    isActive: [{
                        required: true, type: 'string', message: '状态不能为空', trigger: 'change',
                    }]
                }
            }
        },
        computed: {
            ...mapGetters({
                user: 'user'
            })
        },
        methods: {
            showAddUserWin: function () {
                this.resetForm();
                this.editModalWin = true;
            },
            showEditUserWin: function (row) {
                row.isActive=row.isActive==true?'true':'false';
                this.formParameter = {
                    id:row._id,
                    name: row.name,
                    key: row.key,
                    value: row.value,
                    notes: row.notes,
                    isActive: row.isActive
                };
                this.editModalWin = true;
            },
            queryParameter: function () {
                this.$api.get('/parameter/queryParameter', {
                    name:this.name,
                    key:this.key,
                    page: this.page,
                    size: this.size
                }).then(res => {
                    this.total = res.data.total;
                    this.lists = res.data.rs;
                });
            },
            cancelModel: function(name){
                this.$refs[name].resetFields();
                this.editModalWin = false;
            },
            saveUser: function(name){
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if(this.formParameter.id == ''){
                            console.log(this.formParameter)
                            this.$api.post('/parameter/saveParameter', this.formParameter).then(response => {
                                this.$Message.success('增加参数成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                                this.queryParameter();
                            });
                        }else{
                            this.$api.post('/parameter/modifyParameter', this.formParameter).then(response => {
                                this.$Message.success('修改参数成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                                this.queryParameter();
                            });
                        }
                    }
                })
            },
            displayParameter(row){
                this.$api.post('/parameter/displayParameter',{id:row._id,
                isActive:!row.isActive,
                key:row.key,
                value:row.value}).then(res=>{
                    this.queryParameter();
                })
            },
            removeParameter: function (row) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>是否确认删除？</p>',
                    onOk: () => {
                        this.$api.post('/parameter/removeParameterById', {id: row._id}).then(response => {
                            this.$Message.success('删除成功!');
                            this.queryParameter();
                        });
                    }
                });
            },
            changePage: function (page) {
                this.page = page;
                this.queryParameter();
            },
            changePageSize: function (size) {
                this.size = size;
                this.queryParameter();
            },
            resetForm: function () {
                this.formParameter = {
                    id:'',
                    name: '',
                    key: '',
                    value: '',
                    notes: '',
                    isActive: ''
                };
            }
        },
        mounted(){
            /*if (this.user.roleIds[0] != 1) {
                //非系统管理员不能操作，跳转到没有操作权限页面
                this.$router.push(`/error`);
            }*/
            if (this.user != null) {
                this.queryParameter();
            }
        }
    }
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/common.less';
@import '~@/style/user.less';
</style>
