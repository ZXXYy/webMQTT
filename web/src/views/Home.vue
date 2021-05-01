<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
              mode="inline"
              style="height: 100%"
      >
        <a-sub-menu key="sub1">
          <template #title>
            <span>
              <user-outlined />
              设置
            </span>
          </template>
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
          <template #title>
            <span>
              <laptop-outlined />
              模拟调试
            </span>
          </template>
          <router-link to="/Model">
            <a-menu-item key="/Model">
              物模型数据
            </a-menu-item>

          </router-link>
          <router-link to="/Simulate">
            <a-menu-item key="/Simulate">
              定时模拟
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
