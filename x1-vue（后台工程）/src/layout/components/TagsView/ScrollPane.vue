<template>
  <div class="scroll-container">
    <div ref="wrap" class="scroll-wrap" @wheel.native.prevent="handleScroll">
      <slot />
    </div>
  </div>
</template>

<script>

export default {
  name: 'ScrollPane',
  data() {
    return {
      left: 0
    }
  },
  computed: {
    scrollWrapper() {
      return this.$refs.wrap
    }
  },
  mounted() {
    this.scrollWrapper.addEventListener('scroll', this.emitScroll, true)
  },
  beforeDestroy() {
    this.scrollWrapper.removeEventListener('scroll', this.emitScroll)
  },
  methods: {
    handleScroll(e) {
      const eventDelta = e.wheelDelta || -e.deltaY * 40
      const $scrollWrapper = this.scrollWrapper
      $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
    },
    emitScroll() {
      this.$emit('scroll')
    },
    moveToTarget(currentTag) {
      const $scrollWrapper = this.scrollWrapper
      const containerWidth = $scrollWrapper.offsetWidth // 获取滚动容器的宽度
      const tagList = Array.from(this.$parent.$refs.tag)

      const tagAndTagSpacing = 4 // 标签之间的间距
      let firstTag = null
      let lastTag = null

      if (tagList.length > 0) {
        firstTag = tagList[0]// 获取第一个标签
        lastTag = tagList[tagList.length - 1] // 获取最后一个标签
      }

      if (firstTag === currentTag) {
        $scrollWrapper.scrollLeft = 0
      } else if (lastTag === currentTag) {
        $scrollWrapper.scrollLeft = $scrollWrapper.scrollWidth - containerWidth
      } else {
        // 获取当前标签的上一个跟下一个标签
        const currentIndex = tagList.findIndex(item => item === currentTag)
        const prevTag = tagList[currentIndex - 1]
        const nextTag = tagList[currentIndex + 1]

        // 标签的左偏移在nextTag之后
        const afterNextTagOffsetLeft = nextTag.$el.offsetLeft + nextTag.$el.offsetWidth + tagAndTagSpacing
        // 标签的左偏移在prevTag之前
        const beforePrevTagOffsetLeft = prevTag.$el.offsetLeft - tagAndTagSpacing

        if (afterNextTagOffsetLeft > $scrollWrapper.scrollLeft + containerWidth) {
          $scrollWrapper.scrollLeft = afterNextTagOffsetLeft - containerWidth
        } else if (beforePrevTagOffsetLeft < $scrollWrapper.scrollLeft) {
          $scrollWrapper.scrollLeft = beforePrevTagOffsetLeft
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;

  .scroll-wrap{
    width: 100%;
    height: 40px;
    overflow-x: auto;

    &::-webkit-scrollbar{
      display: none;
    }

    &:hover::-webkit-scrollbar{
      display: block;
      height: 6px;
    }

    &::-webkit-scrollbar-thumb{
     border-radius: 5px;
     background-color: #c7c9cca9;
    }

    &::-webkit-scrollbar-track{
     border-radius: 0px;
    }
  }
}
</style>

