<template>
    <div class="role-box">
        <!-- 角色列表 -->
         <div class="header">
             <label>用户名称:</label><Input placeholder="用户名称" style="width: 180px;"></Input>                    
                                 
                 <Button class="role-btn" type="primary">
                     <Icon type="ios-search-outline" />查询
                 </Button>
                 <Button type="primary" @click="showAddRole=true">
                  <Icon type="ios-add" />新增</Button>
         </div> 
        <div>
              <!-- table栏页面 -->
            <Table with="550px" border :columns="cols" :data="roleDatas"></Table>
           
        </div>   
         <!-- 分页 -->
        <Row>
            <Page :total="total" show-sizer show-total show-elevator @on-page-size-change="pageSizeChange" :current="search_type?search_active:active" @on-change="indexChange" :page-size="10"/>
        </Row>        
        
    <!-- 添加角色 -->
        <modal v-model="showAddRole" title="添加角色" @on-ok="ok"  @on-cancel="cancel" width="400" height="150">  
            <Form :rules="userValidate" :label-width="83">
                <div v-if="addCode == true">
                    <FormItem label="角色名称:" >
                        <Input  placeholder="请输入角色名称" style="width: 260px;"></Input>
                    </FormItem>
                </div>
            </Form>                     
       </modal>
    </div>
</template>

<script>
    export default {    
        data() {
            return {   
                 total: 0,
                page: 1,
                size: 10,           
                roleDatas:[],  
                 showAddRole: false,                
                cols: [
                     {type: 'index',                        
                        width: 60,
                        align: 'left',                      
                     },                   
                    {
                        title: '角色名称',
                        key: 'name',
                        width: 160,
                         align: 'left',                       
                    },
                    {
                        title: '归属',
                        key: 'notes',
                        width: 160,
                         align: 'left'
                    },
                    {
                        title: '创建时间',
                        key: 'updateDate',
                        width: 160,
                         align: 'left'
                    },                                        
                   {
                        title: '操作',
                        key: 'action',
                        width: 230,
                        align: 'left',
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'Large',
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
                                }, '权限分配')
                            ]);
                        }
                    }
                ],               
            }
        },
        
        methods:{
            ok () {
                this.$Message.info('Clicked ok');
            },
            cancel () {
                this.$Message.info('Clicked cancel');
            },          
            queryRole (val) {
                this.$api.get('/role/queryRole', {
                    page: val ? 1 : this.page,
                    size: this.size
                    
                }).then(response => {
                    this.total = response.data.total;
                    this.roleDatas = response.data.rs;
                     console.log(response);
                });
            },
            changePage: function (page) {
                this.page = page;
                this.queryRole();
            },
            changePageSize: function (size) {
                this.size = size;
                this.queryRole();
            },
        },
        // 
          removeRole: function (row) {
              
            },
         mounted(){                     
            this.queryRole();      
        }
    }
</script>

<style lang="less" scoped>
 .role-box{
   width: 700px;   
  margin-top: 96px;
}
.header{
  padding: 20px;
}
.role-btn{
  margin: 0 40px;
}

</style>