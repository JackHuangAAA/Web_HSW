<template>
    <div class="param-box">
       <!-- 参数列表 -->
         <div class="header">
             <label>名称:</label><Input placeholder="请输入中文名称" style="width: 180px;"></Input>                    
             <label>使用名称:</label><Input placeholder="请输入英文名称" style="width: 180px;"></Input>                       
                 <Button class="param-btn" type="primary" @click="getParams">
                     <Icon type="ios-search-outline" />查询
                 </Button>
                 <Button type="primary" @click="showAddRole=true">
                  <Icon type="ios-add" />新增</Button>
         </div>  
   
         <div>
              <!-- table栏页面 -->
            <Table with="550px" border :columns="cols" :data="paramDatas"></Table>
         </div>  

          <!-- 分页 -->
        <Row>
            <Page :total="total" show-sizer show-total show-elevator @on-page-size-change="pageSizeChange" :current="search_type?search_active:active" @on-change="indexChange" :page-size="10"/>
        </Row>     
    </div> 
</template>

<script>
    export default {
        data() {
            return {              
                 total: 0,
                page: 1,
                size: 10,           
                paramDatas:[],  
                 showAddRole: false,                
                cols: [
                     {type: 'index',                        
                        width: 60,
                        align: 'left',                      
                     },                   
                    {
                        title: '名称',
                        key: 'name',
                        width: 160,
                         align: 'left',                       
                    },
                    {
                        title: '使用名称',
                        key: 'notes',
                        width: 160,
                         align: 'left'
                    },
                    {
                        title: '参数值',
                        key: 'updateDate',
                        width: 160,
                         align: 'left'
                    },  
                     {
                        title: '说明',
                        key: 'updateDate',
                        width: 160,
                         align: 'left'
                    },  
                      {
                        title: '状态',
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
                                }, '停用')
                            ]);
                        }
                    }
                ],               
            }          
         },
         methods: {
              queryParam (val) {
                this.$api.get('/summary/querySummary', {
                    page: val ? 1 : this.page,
                    size: this.size
                    
                }).then(response => {
                    this.total = response.data.total;
                    this.paramDatas = response.data.rs;
                     console.log(response);
                });
            },
            changePage: function (page) {
                this.page = page;
                this.queryParam();
            },
            changePageSize: function (size) {
                this.size = size;
                this.queryParam();
            },
         },
           mounted(){                     
            this.queryParam();      
        }
    }
</script>

<style lang="less" scoped>
 .param-box{  
  margin-top: 96px;
}
.header{
  padding: 20px;
}
.param-btn{
  margin: 0 40px;
}
</style>