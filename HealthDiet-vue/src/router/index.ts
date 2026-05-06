import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/index',
      name: 'index',
      component: () => import('../views/Index.vue'),
      redirect: '/index/welcome',
      children: [
        {
          path: 'welcome',
          name: 'welcome',
          component: () => import('../views/WelcomeView.vue')
        },
        {
          path: 'diet',
          name: 'diet',
          component: () => import('../views/DietRecord.vue')
        },
        {
          path: 'recordList',
          name: 'recordList',
          component: () => import('../views/DietRecordList.vue')
        },
        {
          path: 'personalCenter',
          name: 'personalCenter',
          component: () => import('../views/PersonalCenter.vue')
        },
        {
          path: 'healthTrend',
          name: 'healthTrend',
          component: () => import('../views/HealthTrend.vue')
        },

        {
          path: "recipeCenter",
          name: "recipeCenter",
          component: () => import("@/views/RecipeCenter.vue")
        },
        {
          path: "/foodManage",
          name: "foodManage",
          component: () => import("@/views/FoodManage.vue")
        },
        {
          path: "/dishManage",
          name: "dishManage",
          component: () => import("@/views/DishManage.vue")
        },
        {
          path: "myDishManage",
          name: "myDishManage",
          component: () => import("@/views/MyDishManage.vue")
        },
        {
          path: "nutritionistDashboard",
          name: "nutritionistDashboard",
          component: () => import("@/views/NutritionistDashboard.vue")
        },
        {
          path: "/userManage",
          name: "userManage",
          component: () => import("@/views/UserManage.vue")
        },
        {
          path: "/announcementManage",
          name: "announcementManage",
          component: () => import("@/views/AnnouncementManage.vue")
        },
        {
          path: "/healthNewsManage",
          name: "healthNewsManage",
          component: () => import("@/views/HealthNewsManage.vue")
        },
        {
          path: "chat",
          name: "chat",
          component: () => import("@/views/ChatPage.vue")
        },
        {
          path: "chat/:sessionId",
          name: "chatWindow",
          component: () => import("@/views/ChatWindow.vue")
        },
        {
          path: "aiChat",
          name: "aiChat",
          component: () => import("@/views/AiChat.vue")
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const user = localStorage.getItem("user")

  if (!user && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router