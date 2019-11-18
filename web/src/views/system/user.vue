<template>
    <div class="main-table">
        <!--用户列表-->
        <Row>
            <Col offset="14" span="6" class="main-table-search">
                <div style="width:88px; font-size:15px">用户名称:</div>                    
                <input  v-model="userName" placeholder="用户名称"  @keyup.enter="queryUsers()"></input>   
            </Col>
            <Col span="4" style="display:flex">
                <Button type="primary" class="search_btn" icon="ios-search" @click="queryUsers()">查询</Button>
                <Button type="primary" class="search_btn" icon="ios-add" @click="showAddUserWin">新增</Button>
            </Col>
        </Row>
        <Table :columns="cols" :data="userDatas" size="small" max-height=435 class="table-mt" stripe border></Table>
        <Row>
            <Page :current="page" :page-size="size" :total="total"
                  @on-change="changePage" @on-page-size-change="changePageSize" show-total show-sizer></Page>
        </Row>
        <!--编辑用户-->
        <Modal v-model="editModalWin" title="编辑用户信息" :footerHide="true" width="400" height="150" :closable="false">
            <Form ref="frmEdit" :model="formUser" :rules="userValidate" :label-width="83" >
                <div v-if="addCode == true">
                    <FormItem label="登录账号:" prop="code">
                        <Input v-model="formUser.code" placeholder="请输入账号" style="width: 260px;"></Input>
                    </FormItem>
                </div>
                <div v-if="addCode == false">
                    <FormItem label="登录账号:" prop="code">
                        <Input v-model="formUser.code" placeholder="请输入账号" disabled style="width: 260px;"></Input>
                    </FormItem>
                </div>
                <FormItem label="用户名称:" prop="name">
                    <Input v-model="formUser.name" placeholder="请输入用户名称" style="width: 260px;"></Input>
                </FormItem>
                <FormItem label="移动电话:" prop="phone">
                    <Input v-model="formUser.phone" placeholder="请输入移动电话号" :maxlength="11" style="width: 260px;"></Input>
                </FormItem>
                <FormItem label="角色" prop="role">
                    <Select v-model="formUser.role" style="width:260px">
                        <Option v-for="item in roleDatas" :value="item._id" :key="item.id">{{ item.name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="备注">
                    <Input v-model="formUser.notes" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                           placeholder="请输入..." style="width: 260px;"></Input>
                </FormItem>
                <div align="right">
                    <FormItem style="margin-top: 15px;">
                        <Button  @click="cancelModel('frmEdit')">取消</Button>
                        <Button type="primary" @click="saveUser('frmEdit')">保存</Button>
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
                userName:'',
                addCode: true,
                editModalWin: false,
                roleDatas:[],
                userDatas: [],
                cols: [
                    {
                        type: 'index',
                        width: 60,
                        align: 'center'
                    },{
                        title: '手机号码',
                        key: 'phone',
                        align: 'center'
                    },{
                        title: '用户名',
                        key: 'name',
                        align: 'center'
                    },{
                        title: '角色',
                        key: 'role',
                        align: 'center',
                        render:(h,params)=>{
                            return h(
                                'div',
                                params.row.role!=undefined?params.row.role.name:''
                            )
                        }
                    },{
                        title: '更新时间',
                        key: 'updateDate',
                        align: 'center'
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
                                            this.removeUser(params.row)
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
                                            this.resetPwd(params.row)
                                        }
                                    }
                                }, '密码初始化')
                            ]);
                        }
                    }
                ],
                formUser: {
                    id:'',
                    name: '',
                    code: '',
                    phone: '',
                    role: '',
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
                this.addCode = true;
                this.resetForm();
                this.editModalWin = true;
            },
            showEditUserWin: function (row) {
                this.addCode = false;
                console.log(row.role.name)
                this.formUser = {
                    id:row._id,
                    name: row.name,
                    code: row.code,
                    phone: row.phone,
                    role: row.role._id,
                    notes: row.notes
                };
                // this.loadRoles();
                this.editModalWin = true;
            },
            queryUsers: function () {
                this.$api.get('/user/queryUsers', {
                    name: this.userName,
                    page: this.page||1,
                    size: this.size||10
                }).then(res => {
                    res.data.rs.forEach(item=>{
                        item.updateDate=moment(item.updateDate).format('YYYY-MM-DD HH:mm:ss');
                    });
                    this.total = res.data.total;
                    this.userDatas = res.data.rs;
                });
                
                this.$api.get('/role/queryRole').then(res=>{
                    this.roleDatas=res.data.rs;
                });
            },
            cancelModel: function(name){
                this.$refs[name].resetFields();
                this.editModalWin = false;
            },
            saveUser: function(name){
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if(this.formUser.id == ''){
                            this.$api.post('/user/saveUser', this.formUser).then(response => {
                                this.$Message.success('增加用户成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                                this.queryUsers();
                            });
                        }else{
                            this.$api.post('/user/modifyUser', this.formUser).then(response => {
                                this.$Message.success('修改用户成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                                this.queryUsers();
                            });
                        }
                    }
                })
            },
            removeUser: function (row) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>是否确认删除？</p>',
                    onOk: () => {
                        this.$api.post('/user/removeUserById', {id: row._id}).then(response => {
                            this.$Message.success('删除成功!');
                            this.queryUsers();
                        });
                    }
                });
            },
            resetPwd: function (row) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>确认重置密码？</p>',
                    onOk: () => {
                        this.$api.post('/user/resetUser', {id: row.id}).then(response => {
                            this.$Message.success('密码重置成功!');
                        });
                    }
                });
            },
            changePage: function (page) {
                this.page = page;
                this.queryUsers();
            },
            changePageSize: function (size) {
                this.size = size;
                this.queryUsers();
            },
            resetForm: function () {
                this.formUser = {
                    id:'',
                    name: '',
                    code: '',
                    phone: '',
                    role: '',
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
                this.queryUsers();
            }
        }
    }
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/common.less';
</style>
