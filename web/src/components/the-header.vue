<template>
    <a-layout-header class="header">
        <img class="logo" src="@/assets/mqtt-logo.png"/>
        <a-menu
                theme="dark"
                mode="horizontal"
                :default-selected-keys="['2']"
                :style="{ lineHeight: '64px' }"
        >
            <a-menu-item key="/">
                <router-link to="/">首页</router-link>
            </a-menu-item>

            <a-menu-item key="/about">
                <router-link to="/about">关于我们</router-link>
            </a-menu-item>
        </a-menu>
    </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'the-header'
});
</script>