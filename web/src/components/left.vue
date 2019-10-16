<template>
    <div class="layout-menu-left">
         <Menu ref="menuView" theme="light" width="auto" accordion @on-select="selectMenu" id='menu-list'>
            <template v-for="(menu,index) in menus">
                <MenuItem v-if="!menu.children || menu.children.length === 0" :name="menu.url || '' "  :key="index"  style="font-size: 15px;">
                <Icon :type="menu.icon" :key="index" /> {{menu.title}}
                </MenuItem>
                <Submenu :name="index" v-else>
                    <template slot="title">
                        <Icon :type="menu.icon"></Icon>
                        {{menu.title}}
                    </template>
                    <MenuItem v-for="item in menu.children" :name="item.url" :key="item._id" style="font-size: 15px;">{{item.title}}
                    </MenuItem>
                </Submenu>
            </template>
        </Menu> 
    </div>
</template>

<style lang="less" >

</style>

<script>
import { mapGetters, mapActions } from 'vuex'
import md5 from 'js-md5'

export default {
    data() {
        return {}
    },
    props: {
        menus: Array
    },
    computed: {
        ...mapGetters({
            currentMenu: 'currentMenu'
        })
    },
    components: {

    },
    watch: {
        '$route.path': function(n, o) {
            this.currentPath = n
        }
    },
    methods: {
        ...mapActions({
            setCurrentMenu: 'setCurrentMenu'
        }),
        selectMenu: function(url) {
            this.loading = true;
            this.setCurrentMenu({
                url: url
            });
            this.$router.push(url);
            this.loading = false;
        },

    },
    updated() {
        this.$nextTick(() => {
            if (this.$refs.menuView) {
                this.$refs.menuView.updateOpened();
            }
        });
    },
    mounted() {
    }
}
</script>
<style lang="less" scoped>
    @import "~@/style/layout.less";
</style>

