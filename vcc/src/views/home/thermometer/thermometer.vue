<template>
  <div class="main-container"
       :class="{'grabbing': dragging}"
       @mousemove="mouseMoving"
       @mouseUp="stopDrag">
    <div class="upper-container"
         :style="bgStyle">
      <h2 class="temperature-text">{{currentTemperature | round}}</h2>
      <div class="temperature-graduation">
        <div class="temperature-element"
             v-for="el in temperatureGrades"
             :key="el"
             :style="tempElementStyle(el)">
          <span class="temperature-element-number">{{el}}</span><br>
          <span class="temperature-element-line">|</span>
        </div>
      </div>
    </div>
    <div class="lower-container">
      <div class="slider-container"
           :style="sliderStyle">
        <svg>
          <path d="M74.3132 0C47.0043 2.44032e-05 50.175 30 7.9179 30H144.27C99.4571 30 101.622 -2.44032e-05 74.3132 0Z"
                transform="translate(-7.38794 0.5)"
                fill="#12132C" />
        </svg>
        <div class="slider-button"
             :class="{'grabbing': dragging}"
             @mouseDown="startDrag">
          <i class="fas fa-thermometer-empty slider-icon"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const sliderMinX = 0
const sliderMaxX = 240

const coldGradient = { start: '#5564C2', end: '#3A2E8D' }
const hotGradient = { start: '#F0AE4B', end: '#9B4D1B' }
import './TweenMax.min.js'
export default {
  name: 'thermometer',
  data () {
    return {
      dragging: false,
      initialMouseX: 10,
      sliderX: 10,
      initialSliderX: 10,
      temperatureGrades: [10, 15, 20, 25, 30],
      gradientStart: coldGradient.start,
      gradientEnd: coldGradient.end
    }
  },
  filters: {
    round (num) {
      return Math.round(num)
    }
  },
  methods: {
    startDrag (e) {
      this.dragging = true
      this.initialMouseX = e.pageX
      this.initialSliderX = this.sliderX
    },
    stopDrag () {
      this.dragging = false
    },
    mouseMoving (e) {
      if (this.dragging) {
        const dragAmount = e.pageX - this.initialMouseX
        const targetX = this.initialSliderX + dragAmount

        // keep slider inside limits
        this.sliderX = Math.max(Math.min(targetX, sliderMaxX), sliderMinX)

        // set bg color
        let targetGradient = coldGradient
        if (this.currentTemperature >= 25) {
          targetGradient = hotGradient
        }

        if (this.gradientStart !== targetGradient.start) {
          // gradient changed
          TweenLite.to(this, 0.7, {
            'gradientStart': targetGradient.start,
            'gradientEnd': targetGradient.end
          })
        }
      }
    },
    tempElementStyle (tempNumber) {
      const nearDistance = 3
      const liftDistance = 12

      // lifts up the element when the current temperature is near it
      const diff = Math.abs(this.currentTemperature - tempNumber)
      const distY = (diff / nearDistance) - 1

      // constrain the distance so that the element doesn't go to the bottom
      const elementY = Math.min(distY * liftDistance, 0)
      return `transform: translate3d(0, ${elementY}px, 0)`
    }
  },
  computed: {
    currentTemperature () {
      const tempRangeStart = 10
      const tempRange = 20 // from 10 - 30
      return (this.sliderX / sliderMaxX * tempRange) + tempRangeStart
    },
    sliderStyle () {
      return `transform: translate3d(${this.sliderX}px,0,0)`
    },
    bgStyle () {
      return `background: linear-gradient(to bottom right, ${this.gradientStart}, ${this.gradientEnd});`
    }
  }
}
</script>

<style lang="less">
.main-container {
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 3fr 1fr;
  height: 100vh;
  overflow-x: hidden;
}
.upper-container {
  position: relative;
  background: linear-gradient(to bottom right, #5564c2, #3a2e8d);
}
.temperature-text {
  position: absolute;
  bottom: 150px;
  user-select: none;
  font-size: 100px;
  width: 100%;
  text-align: center;
}
.temperature-graduation {
  left: calc(50% - 150px);
  position: absolute;
  bottom: 25px;
  user-select: none;
}
.temperature-element {
  text-align: center;
  display: inline-block;
  width: 40px;
  margin: 0 10px 0 10px;
  opacity: 0.7;
}

.temperature-element-line {
  font-size: 7px;
}

.lower-container {
  background-color: #12132c;
}
.slider-container {
  width: 150px;
  height: 80px;
  margin-top: -30px;
  margin-left: calc(50% - 187px);
  position: relative;
}
.slider-button {
  position: absolute;
  left: 42px;
  top: 5px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #2724a2;

  cursor: grab;
  cursor: -webkit-grab;
  cursor: -moz-grab;
}
.grabbing {
  cursor: grabbing;
  cursor: -webkit-grabbing;
  cursor: -moz-grabbing;
}

.slider-icon {
  margin-top: 16px;
  margin-left: 21px;
  color: white;
}
</style>