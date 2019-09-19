import Vue from 'vue'
import VueRouter from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import ManageMember from '../components/manage/member/ManageMember'

Vue.use(VueRouter)

export default new VueRouter({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/manage/member',
      name: 'ManageMember',
      component: ManageMember
    }
  ]
})
