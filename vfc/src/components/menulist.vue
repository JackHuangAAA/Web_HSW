<template>
  <transition>
    <div class="menulist"
         :class="{'menuopen':menuOpen,'menuclose':!menuOpen}">
      <Row>
        <div class="menu-button"
             @click="buttonClick()">
          <div class="menu-icon"
               :class="{'icon-open':!menuOpen,'icon-close':menuOpen}"></div>
          <div class="menu-status">{{menustatus}}</div>
        </div>
      </Row>
      <Row>
        <div class="menu-logo">
        </div>
        <div class="menu-logo-info">银信疫苗接种平台</div>
      </Row>
      <Row>
        <div class="menu-location">
          <div class="menu-location-icon"></div>
          <div class="menu-location-info">{{location}}</div>
        </div>
      </Row>
      <Row>
        <div class="menu-main-title">菜单列表</div>
      </Row>
      <Row>
        <div class="menu-item"
             v-for="item in lists"
             :class="{'menu-item-active':item.name==activeMenu}"
             @click="menuClick(item)">
          <div class="menu-item-icon"><img :src="menuIcon(item.icon)"
                 alt=""></div>
          <div class="menu-item-title">{{item.title}}</div>
        </div>
      </Row>
    </div>
  </transition>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "menulist",
  props: {
    lists: {
      type: Array,
      default() {
        return [];
      }
    },
    menuOpen: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      activeMenu: "home"
    };
  },
  computed: {
    location(){
      return this.$store.state.device.address.countyName
    },
    menustatus() {
      return this.menuOpen === false ? "展开菜单" : "折叠菜单";
    }
  },
  mounted() {},
  methods: {
    menuIcon(icon) {
      return `static/img/icon/${icon}.png`;
    },
    menuClick(item) {
      this.$emit("clickitem", item);
      let route = item.name;
      if (typeof item.main !== "undefined") {
        route = item.main;
      }
      this.activeMenu = item.name;
      this.$router.push({ name: route });
    },
    buttonClick() {
      this.$emit("click", this.menuOpen);
    }
  }
};
</script>

<style lang="less">
@import "~@/style/menulist.less";
</style>