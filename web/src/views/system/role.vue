<template>
    <div class="main-table">
        <!--用户列表-->
        <Row>
            <Col offset="14" span="6" class="main-table-search">
            <div style="width:88px; font-size:15px">用户名称:</div>
            <input  v-model="name" placeholder="用户名称"  @keyup.enter="queryRole()"></input>
            </Col>
            <Col span="4" style="display:flex">
            <Button type="primary" class="search_btn" icon="ios-search" @click="queryRole()">查询</Button>
            <Button type="primary" class="search_btn" icon="ios-add" @click="showAddUserWin">新增</Button>
            </Col>
        </Row>
        <Table :columns="cols" :data="lists" size="small" max-height=435 class="table-mt role-table" stripe border></Table>
        <div class="permissionTree" :class="{'permissionStyle':permissionShow}">
            <div class="permissionTree-title">企业测试权限分配</div>
            <Tree :data="tree" @on-check-change="getTreeData" show-checkbox></Tree>
            <Button class="savePermission" type="primary" @click="savePermission">保存</Button>
            <Button class="resetPermission" type="info" @click="getTree">重置</Button>
            <Button class="closePermission" type="default" @click="closePermission">关闭</Button>
        </div>
        <Row>
            <Page :current="page" :page-size="size" :total="total" class="role-page"
                  @on-change="changePage" @on-page-size-change="changePageSize" show-total show-sizer></Page>
        </Row>
        <!--编辑用户-->
        <Modal v-model="editModalWin" title="编辑用户信息" :footerHide="true" width="400" height="150" :closable="false">
            <Form ref="frmEdit" :model="formRole" :rules="userValidate" :label-width="83" >
                <div>
                    <FormItem label="名称:" prop="name">
                        <Input v-model="formRole.name" placeholder="请输入名称" style="width: 260px;"></Input>
                    </FormItem>
                </div>
                <FormItem label="归属:" prop="type">
                    <Select v-model="formRole.type" clearable style="width:260px">
                        <Option v-for="item in roleType" :value="item.value" :key="item.key">{{ item.name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="说明">
                    <Input v-model="formRole.notes" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                           placeholder="请输入..." style="width: 260px;"></Input>
                </FormItem>
                <div align="right">
                    <FormItem style="margin-top: 15px;">
                        <Button  @click="cancelModel('frmEdit')">取消</Button>
                        <Button type="primary" @click="saveRole('frmEdit')">保存</Button>
                    </FormItem>
                </div>
            </Form>
        </Modal>
    </div>
</template>
<script>
    import Button from 'iview/src/components/button'
    import {mapGetters} from 'vuex'
    import moment from 'moment'
    export default{
        data(){
            const checkCode = (rule, value, callback, source, options) => {
                if(this.addCode == true){
                    this.$api.get('/user/queryUsers', {code: value}).then(res => {
                        if (res.data.total == 1) {
                            callback(new Error("账号已被占用"));
                        } else {
                            callback();
                        }
                    })
                }else{
                    callback();
                }
            };
            return {
                total: 0,
                page: 1,
                size: 10,
                name:'',
                editModalWin: false,
                roleType:[
                    {name:"运营",value:0,key:1},
                    {name:"医院",value:1,key:2}
                ],
                lists: [],
                tree: [],
                cols: [
                    {
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },{
                        title: '角色名称',
                        key: 'name',
                        align: 'center'
                    },{
                        title: '归属',
                        key: 'type',
                        align: 'center',
                        render:(h,params)=>{
                            return h(
                                'div',
                                params.row.type==0?'运营':'医院'
                            )
                        }
                    },{
                        title: '创建时间',
                        key: 'createDatele',
                        align: 'center',
                        render:(h,params)=>{
                            return h(
                                'div',
                                params.row.createDate
                            )
                        }
                    },{
                        title: '操作',
                        key: 'action',
                        width:200,
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
                                            this.removeRoleById(params.row)
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
                                            this.resetPermission(params.row)
                                        }
                                    }
                                }, '权限分配')
                            ]);
                        }
                    }
                ],
                formRole: {
                    id:'',
                    name: '',
                    type: '',
                    notes: ''
                },
                userValidate: {
                    code : [{
                        required: true, message: '账号不能为空', trigger: 'blur'
                    },{
                        type: 'string',min: 4, message: '账号过短', trigger: 'blur'
                    },{
                        type: 'string', max: 30, message: '账号超长', trigger: 'blur',
                    },{validator: checkCode}],
                    name: [{
                        required: true, message: '用户名称不能为空', trigger: 'blur'
                    },{
                        type: 'string', max: 20, message: '用户名超长',trigger: 'blur'
                    }
                    ],
                    phone: [{
                        required: true, message: '移动电话不能为空', trigger: 'blur'
                    },{
                        type: 'string', min: 11, message: '移动电话号超短', trigger: 'blur'
                    },{
                        type: 'string', max: 11, message: '移动电话号超长', trigger: 'blur'
                    },{
                        validator(rule, value, callback, source, options) {
                            var errors = [];
                            if (!/^[0-9]{11}$/.test(value)) {
                                callback('手机号输入错误');
                            }
                            callback(errors);
                        }
                    }],
                    role: [{
                        required: true, type: 'string', message: '角色不能为空', trigger: 'change',
                    }]
                },
                permissionShow:false,
                userInfo:{},
                treeData:[]
            }
        },
        computed: {
            ...mapGetters({
                user: 'user'
            })
        },
        methods: {
            getTreeData(data){
                console.log(data);
                this.treeData=data;
            },
            showAddUserWin: function () {
                this.resetForm();
                this.editModalWin = true;
            },
            showEditUserWin: function (row) {
                this.formRole = {
                    id:row._id,
                    name:row.name,
                    type:row.type,
                    notes:row.notes
                };
                this.editModalWin = true;
            },
            queryRole: function () {
                this.$api.get('/role/queryRole', {
                    name: this.name,
                    page: this.page||1,
                    size: this.size||10
                }).then(res => {
                    res.data.rs.forEach(item=>{
                        item.createDate=moment(item.createDate).format('YYYY-MM-DD HH:mm:ss');
                    });
                    this.total = res.data.total;
                    this.lists = res.data.rs;
                });
            },
            cancelModel: function(name){
                this.$refs[name].resetFields();
                this.editModalWin = false;
            },
            saveRole: function(name){
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if(this.formRole.id == ''){
                            this.$api.post('/role/saveRole', this.formRole).then(response => {
                                this.$Message.success('增加用户成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                                this.queryRole();
                            });
                        }else{
                            this.$api.post('/role/modifyRole', this.formRole).then(response => {
                                this.$Message.success('修改用户成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                                this.queryRole();
                            });
                        }
                    }
                })
            },
            savePermission(){
                if(this.userInfo.permission){
                    this.$api.post('/permission/modifyPermission',{menuData:this.treeData,id:this.userInfo._id}).then(res=>{
                        this.queryRole();
                    });
                }else{
                    this.$api.post('/permission/savePermission',{menuData:this.treeData,id:this.userInfo._id}).then(res=>{
                        this.queryRole();
                    });
                }
            },
            removeRoleById: function (row) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>是否确认删除？</p>',
                    onOk: () => {
                        this.$api.post('/role/removeRoleById', {id: row._id}).then(response => {
                            this.$Message.success('删除成功!');
                            this.queryRole();
                        });
                    }
                });
            },
            resetPermission:async function (row) {
                await this.getTree();
                this.userInfo=row;
                this.permissionShow=true;
                if(row.permission){//当前角色是否拥有权限
                    let tree=this.tree;
                    let permission=row.permission.children;
                    let children=[];
                    permission.forEach(item=>{
                        item.children.forEach(el=>{
                            children.push(el);
                        })
                    })
                    tree.forEach(item=>{
                        item.children.forEach(el=>{
                            children.forEach((ele,i)=>{
                                if(el._id==children[i]._id){
                                    this.$set(el,'checked',true);
                                }
                            })
                        })
                    })
                }
            },
            async getTree(){
                await this.$api.get('/menu/queryMenus').then(res=>{
                    this.tree=res.data;
                });
            },
            closePermission(){
                this.permissionShow=false;
            },
            changePage: function (page) {
                this.page = page;
                this.queryRole();
            },
            changePageSize: function (size) {
                this.size = size;
                this.queryRole();
            },
            resetForm: function () {
                this.formRole = {
                    id:'',
                    name: '',
                    type: '',
                    notes: ''
                };
            }
        },
        mounted(){
            /*if (this.user.roleIds[0] != 1) {
             //非系统管理员不能操作，跳转到没有操作权限页面
             this.$router.push(`/error`);
             }*/
            if (this.user != null) {
                this.queryRole();
            }
        }
    }
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/common.less';
</style>
