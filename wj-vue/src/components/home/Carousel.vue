<template>
  <el-card class="card-carousel">
  <el-carousel  class="push" :interval="4000" arrow="always">
    <el-carousel-item v-for="item in items" :key="item.id">
      <a :href=item.link target="_blank">
        <img :src=item.img alt="" class="carousel_img">
      </a>
      <h3>{{item.title}}</h3>
    </el-carousel-item>
  </el-carousel>
  </el-card>
</template>

<style>
/*新增两行*/
  .el-carousel {
    height: 320px !important; /* 新增：确保轮播组件占满父容器 */
  }

  .el-carousel-item {
    height: 100% !important; /* 新增：确保每个轮播项占满轮播组件 */
  }
  .el-carousel__item h3 {
    color: black;
    font-size: 15px;
    /*line-height: 300px;*/
    bottom: 43px;
    float: left;
    position: relative;
  }

  .card-carousel {
    /*margin: 0 auto;*/
    margin-left: 20px;
    width: 680px;
    height: 320px;
  }

  .push {
    /*margin-left: -15px;*/
  }

  .el-carousel__item:nth-child(2n) {
    background: black;
  }

  .el-carousel__item:nth-child(2n+1) {
    background: black;
  }

  .carousel_img {
    height: 100%;
    width: 100%;
    text-align: center;
  }
</style>

<script>
  export default {
    name: 'Carousel',
    data: function () {
      return {
        items: [{
          id: 1,
          title: 'relax and unwind',
          // img: '../../../static/img/carousel/paintToys.png',
          img: require('../../../static/img/carousel/nealFun.png'),
          link: 'https://neal.fun/'},
        {
          id: 2,
          title: 'dream and wander',
          img: require('../../../static/img/carousel/yume.png'),
          link: 'http://yume.ly/?dt_dapp=1'
        },
        {
          id: 3,
          title: 'study-gogogo!',
          img: require('../../../static/img/carousel/code.png'),
          // img: '/static/img/carousel/mazeToys.png',
          link: 'https://www.runoob.com/'
        }]
      }
    }
  }
</script>
