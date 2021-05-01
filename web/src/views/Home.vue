<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
              mode="inline"
              :default-selected-keys="['1']"
              :default-open-keys="['sub1']"
              :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <span slot="title"><a-icon type="user" />设置</span>
          <router-link to="/Connection">
            <a-menu-item key="/Connection">
              连接设置
            </a-menu-item>
          </router-link>

          <router-link to="/PhyModel">
            <a-menu-item key="/PhyModel">
              导入物模型
            </a-menu-item>
          </router-link>
          <router-link to="/PubSub">
            <a-menu-item key="/PubSub">
              订阅主题
            </a-menu-item>
          </router-link>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <span slot="title"><a-icon type="laptop" />模拟调试</span>
          <router-link to="/Model">
            <a-menu-item key="/Model">
              物模型
            </a-menu-item>
          </router-link>

        </a-sub-menu>

      </a-menu>
    </a-layout-sider>
    <router-view/>

  </a-layout>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';

  export default defineComponent({
    name: 'Home',
  });
</script>
