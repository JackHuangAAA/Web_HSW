<template>
  <div class="main-container">
    <div class="upper-container">
      <div class="temperature-graduation">
        <div class="temperature-element"
             v-for="(el,index) in temperatureGrades"
             :key="el"
             :style="tempElementStyle(index)">
          <span class="temperature-element-number">{{el}}</span><br>
          <span class="temperature-element-line">|</span>
        </div>
      </div>
    </div>
    <div class="header-thermometer">
      <p class="header-thermometer-item">
        {{value}}
      </p>
      <p class="header-thermometer-icon">℃</p>
      <div class="header-right">
        <p>正常</p>
        <p>室温：26℃</p>
      </div>
    </div>
    <div class="slider-contont"
         :style="sliderStyle">
      <svg style="height:22px;'">
        <path d="M74.3132 0 C47.0043 2.44032e-05 50.175 22 7.9179 22 H144.27C99.4571 22 101.622 -2.44032e-05 74.3132 0Z"
              transform="translate(-7.38794 0.5)"
              fill="#ffffff" />
      </svg>
      <div class="slider-button">
        <img :src="thermometerPNG">
      </div>
    </div>
  </div>
</template>

<script>
import thermometerPNG from "_a/icon/thermometer.png";
const sliderMinX = 0;
const sliderMaxX = 240;

const coldGradient = { start: "#5564C2", end: "#3A2E8D" };
const hotGradient = { start: "#F0AE4B", end: "#9B4D1B" };
export default {
  name: "thermometer",
  data() {
    return {
      thermometerPNG,
      dragging: false,
      initialMouseX: 0,
      sliderX: 0,
      initialSliderX: 0,
      temperatureGrades: [-3, -2, -1, 0, 1, 2, 3],
      gradientStart: coldGradient.start,
      gradientEnd: coldGradient.end,
      sideheight: 22
    };
  },
  props: {
    value: {
      type: Number,
      default: 0
    }
  },
  filters: {
    round(num) {
      return Math.round(num);
    }
  },
  methods: {
    tempElementStyle(tempNumber) {
      const length = this.temperatureGrades.length;
      const mid = Math.floor(length / 2);
      const elementY = tempNumber === mid ? -this.sideheight + 2 : 0;
      return `transform: translate3d(0, ${elementY}px, 0)`;
    }
  },
  computed: {
    currentTemperature() {
      const tempRangeStart = 10;
      const tempRange = 20; // from 10 - 30
      return (this.sliderX / sliderMaxX) * tempRange + tempRangeStart;
    },
    sliderStyle() {
      return `transform: translate3d(${this.sliderX}px,0,0)`;
    }
  }
};
</script>

<style lang="less">
@import "./thermometer.less";
</style>