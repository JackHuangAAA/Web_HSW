<template>
    <div>
        <!--用户列表-->
        <div class="table main-table">
            <div class="header">
                <Form inline>
                    <FormItem>
                        账号:<Input v-model="search.userCode" placeholder="账号" style="width: 180px;"></Input>
                        用户名称:<Input v-model="search.userName" placeholder="用户名称" style="width: 180px;"></Input>
                        <Button type="primary" icon="search" @click="loadOperators('query')">查询</Button>
                        <Button type="primary" icon="plus" @click="showAddUserWin">新增</Button>
                    </FormItem>
                </Form>
            </div>
            <div class="content">
                <Table border :columns="cols" :data="userDatas" size="small" border stripe></Table>
            </div>
            <div class="footer">
                <Page :current="page" :size="size" :total="total" size="small"
                      @on-change="changePage" @on-page-size-change="changePageSize" show-total show-elevator show-sizer></Page>
            </div>
        </div>
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
                <FormItem label="角色" prop="roles">
                    <Select v-model="formUser.roles" style="width:260px">
                        <Option v-for="item in roleDatas" :value="item._id" :key="item.id">{{ item.name }}</Option>
                    </Select>
                </FormItem>
                <FormItem label="备注">
                    <Input v-model="formUser.remark" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                           placeholder="请输入..." style="width: 260px;"></Input>
                </FormItem>
                <div align="right">
                    <FormItem style="margin-top: 15px;">
                        <Button  @click="cancelModel('frmEdit')">取消</Button>
                        <Button type="primary" @click="addOperator('frmEdit')">保存</Button>
                    </FormItem>
                </div>
            </Form>
        </Modal>
    </div>
</template>
<script>
    import Button from 'iview/src/components/button'
    import {mapGetters} from 'vuex'

    export default{
        data(){
            const checkCode = (rule, value, callback, source, options) => {
                if(this.addCode == true){
                    this.$api.get('/user/checkCode', {code: value}).then(response => {
                        if (response.data == 1) {
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
                search: {
                    userName: '',
                    userCode: ''
                },
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
                        title: '账号',
                        key: 'code',
                        width: 120,
                        align: 'center'
                    },{
                        title: '用户名',
                        key: 'name',
                        width: 160,
                        align: 'center'
                    },{
                        title: '角色',
                        key: 'role',
                        width: 150,
                        align: 'center',
                        render: (h, params) => {
                            return <div> {params.row.role.name} </div>;
                        }
                    },{
                        title: '电话',
                        key: 'phone',
                        width: 150,
                        align: 'center'
                    },{
                        title: '备注',
                        key: 'remark',
                        ellipsis:true,
                        width: 350,
                        align: 'center'
                    },{
                        title: '操作',
                        key: 'action',
                        width: 230,
                        align: 'center',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'primary',
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
                                        type: 'error',
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
                                }, '密码重置')
                            ]);
                        }
                    }
                ],
                formUser: {
                    id: '',
                    name: '',
                    code: '',
                    phone: '',
                    roles: '',
                    remark: ''
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
                    roles: [{
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

                this.formUser = {
                    id: row.id,
                    name: row.name,
                    code: row.code,
                    phone: row.phone,
                    remark: row.remark
                };this.loadRoles();
                this.editModalWin = true;
            },
            queryUsers: function (val) {
                this.$api.get('/user/qyeryUsers', {
                    name: this.search.userName,
                    code: this.search.userCode,
                    page: val ? 1 : this.page,
                    size: this.size
                }).then(response => {
                    this.total = response.data.total;
                    this.userDatas = response.data.rs;
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
                                this.loadOperators();
                                this.$Message.success('增加用户成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
                            });
                        }else{
                            this.$api.post('/user/modifyUser', this.formUser).then(response => {
                                this.loadOperators();
                                this.$Message.success('修改用户成功!');
                                this.$refs[name].resetFields();
                                this.editModalWin = false;
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
                            this.loadOperators();
                            this.$Message.success('删除成功!');
                        });
                    }
                });
            },
            resetPwd: function (row) {
                this.$Modal.confirm({
                    title: '提示',
                    content: '<p>确认重置密码？</p>',
                    onOk: () => {
                        this.$api.post('/user/resetPwd', {id: row.id}).then(response => {
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
            resetForm: function (size) {
                this.formUser = {
                    id: '',
                    name: '',
                    code: '',
                    phone: '',
                    remark: ''
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
