<template>
  <div>
    <el-card class="box-card">
      <div style="font-size: 45px;color: azure;white-space: nowrap">Live the moment.</div>
<!--      <a href="https://github.com/Antabot/White-Jotter" target="_blank">-->
<!--        <img src="../../assets/img/icon/icon6.png" alt="" class="git-link">-->
<!--      </a>-->
<!--      <a href="https://blog.csdn.net/Neuf_Soleil" target="_blank">-->
<!--        <img src="../../assets/img/icon/csdn.png" alt="" class="csdn-link">-->
<!--      </a>-->
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Slogan'
  }
</script>

<style scoped>
  .box-card {
    width: 100%;
    height: 150px;
    /*background-color: #222;
    text-align: center;
    margin-top: 5px;*/
    background-image: url('../../assets/img/icon/background.png'); /* 你的图片路径 */
    background-size: cover; /* 覆盖整个区域 */
    background-position: center; /* 图片居中 */
    background-repeat: no-repeat; /* 不重复 */
    text-align: center;
    margin-top: 5px;
    display: flex;
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
  }

  .slogan {
    width: 100%;
  }
</style>
