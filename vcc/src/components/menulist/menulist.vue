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
    ...mapGetters(["location"]),
    menustatus() {
      return this.menuOpen === false ? "展开菜单" : "折叠菜单";
    }
  },
  mounted() {},
  methods: {
    menuIcon(icon) {
      return `~@/assets/icon/${icon}.png`;
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
@import "~@/style/color.less";
.menu-item-title {
  font-size: 16px;
  font-weight: bold;
  color: @black;
  letter-spacing: 7px;
}
.menu-item-icon {
  width: 20px;
  height: 20px;
  margin-left: 24px;
  margin-right: 27px;
}
.menu-item {
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  height: 46px;
  cursor: pointer;
}
.menu-item:hover {
  background-color: @gray;
}
.menu-item-active {
  background-color: @gray;
}
.menu-main-title {
  font-size: 13px;
  font-weight: bold;
  color: rgba(138, 147, 155, 1);
  margin-left: 24px;
  margin-bottom: 19px;
}
.menu-location-info {
  font-size: 13px;
  font-weight: bold;
  color: @white;
  margin: auto auto auto 8px;
  letter-spacing: 7px;
}
.menu-location-icon {
  width: 12px;
  height: 16px;
  margin: auto 0px auto 15px;
  background-image: url("~@/assets/location-icon.png");
}
.menu-location {
  display: flex;
  width: 98px;
  height: 30px;
  background: @gray;
  border-radius: 15px;
  margin: 30px auto;
  align-items: center;
}
.menu-logo-info {
  display: flex;
  font-size: 13px;
  font-weight: bold;
  color: @black;
  margin-top: 9px;
  justify-content: center;
  align-items: center;
}
.menu-logo {
  position: relative;
  width: 134px;
  height: 18px;
  background-image: url("~@/assets/logo.png");
  background-size: 134px 18px;
  margin: 34px auto 0px auto;
}
.menu-button {
  width: 180px;
  height: 60px;
  display: flex;
  cursor: pointer;
  background-color: @blue;
}
.menulist {
  width: 180px;
  overflow: hidden;
  background-color: @white;
}
.menuopen {
  height: 100%;
}
.menuclose {
  height: 60px;
}
.menu-icon {
  width: 20px;
  height: 22px;
  margin: auto 17px auto 23px;
}
.icon-open {
  background-image: url("~@/assets/menuopen.png");
  background-size: 20px 22px;
}
.icon-close {
  background-image: url("~@/assets/menuclose.png");
  background-size: 20px 22px;
}
.menu-status {
  margin: auto auto auto 0px;
  font-size: 16px;
  font-weight: bold;
  color: @white;
}
</style>