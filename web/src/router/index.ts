import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import Simulate from '../views/Simulate.vue'
import Connection from '@/components/sidebar/Connection.vue'
import PhyModel from '@/components/sidebar/Import.vue'
import PubSub from '@/components/sidebar/Topic.vue'
import Model from '@/components/sidebar/Model.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    redirect: '/Connection',
    children: [{
      path: '/Connection',
      component: Connection,
      name: '连接'
    },
    {
      path: '/PhyModel',
      component: PhyModel,
      name: '导入'
    },
    {
      path: '/PubSub',
      component: PubSub,
      name: '订阅主题'
    },
    {
      path: '/Model',
      component: Model,
      name: '模拟调试'
    },
    {
      path: '/simulate',
      name: 'Simulate',
      component: Simulate
    }
    ]
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
