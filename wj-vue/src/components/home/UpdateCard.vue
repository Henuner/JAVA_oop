<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span style="font-weight: bold;font-size: 20px">主创说</span>
      <!--<el-button style="float: right; padding: 3px 0" type="text">修改</el-button>-->
    </div>
    <div class="block">
      <el-timeline>
        <el-timeline-item timestamp="一个角落" placement="top">
          <el-card>
            <h4>为思考，为分享，为安放所有不切实际的热情</h4>
            <p>For thinking, for sharing, for all impractical passions.</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="三扇门" placement="top">
          <el-card>
            <h4>通往知识，通往日常，通往纯粹的快活。</h4>
            <p>To knowledge, to daily life, to pure delight.</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="一种信念" placement="top">
          <el-card>
            <h4>独思则窄，分享则通。</h4>
            <p>Solo thinking narrows; sharing enlightens.</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="一份邀请" placement="top">
          <el-card>
            <h4>驻足片刻，或许你会发现你也在寻找这里。</h4>
            <p>Stay a while. You might find you've been searching for this place all along.</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="一场对话" placement="top">
          <el-card>
            <h4>始于你的到来，成于你的发言。</h4>
            <p>It begins with your arrival and thrives with your voice.</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </el-card>
</template>

<script>
  export default {
    name: 'UpdateCard'
  }
</script>

<style scoped>
  .box-card {
    margin-top: 5px;
    width: 988px;
    height: 1050px;
    text-align: left;
    background: rgba(255,255,255,0.8);
  }

  .box-card ::v-deep .el-timeline .el-card {
    background: rgba(255,255,255,0.5);
  }
  /* 修改时间线卡片的标题字体 */
  .box-card ::v-deep .el-timeline .el-card h4 {
    color: #333;
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 8px;
  }
</style>
