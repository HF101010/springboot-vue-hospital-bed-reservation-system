import { useCommonStore } from "@/store";
import AdminLayout from "@/views/Admin/Layout/index.vue";
import FrontLayout from "@/views/Front/Layout/index.vue";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import { createRouter, createWebHashHistory } from "vue-router";

NProgress.configure({
  showSpinner: false,
  speed: 1000,
  trickle: false,
});

const publicRoutes = [
  {
    path: "/",
    redirect: "/Login",
  },
  {
    path: "/Login",
    name: "Login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/Register",
    name: "Register",
    component: () => import("../views/Register.vue"),
  },
  {
    path: "/ForgetPassword",
    name: "ForgetPassword",
    component: () => import("../views/ForgetPassword.vue"),
  },
];

const frontRoutes = [
  {
    path: "/Front",
    component: FrontLayout,
    redirect: "/Front/Reservation",
    meta: {
      title: "前台",
      isAdmin: false,
      requiresAuth: true,
    },
    children: [
      {
        path: "/Front/Reservation",
        name: "Reservation",
        meta: {
          title: "床位预约",
          isAdmin: false,
          requiresAuth: true,
        },
        component: () => import("../views/Front/Reservation.vue"),
      },
      {
        path: "/Front/UserCenter",
        name: "UserCenter",
        meta: {
          title: "账号管理",
          isAdmin: false,
          requiresAuth: true,
        },
        component: () => import("../views/Front/Layout/UserCenter.vue"),
      },
      {
        path: "/Front/Notification",
        name: "FrontNotification",
        meta: {
          title: "系统通知",
          isAdmin: false,
          requiresAuth: true,
        },
        component: () => import("../views/Front/Notification.vue"),
      },
      {
        path: "/Front/PasswordEdit",
        name: "FrontPasswordEdit",
        meta: {
          title: "修改密码",
          isAdmin: false,
          requiresAuth: true,
        },
        component: () => import("../views/Front/PasswordEdit.vue"),
      },
    ],
  },
];

const adminRoutes = [
  {
    path: "/Admin",
    name: "Admin",
    redirect: "/Admin/Home",
    component: AdminLayout,
    meta: {
      title: "控制台",
      isAdmin: true,
    },
    children: [
      {
        path: "/Admin/Home",
        name: "AdminHome",
        meta: {
          title: "控制台",
          isAdmin: true,
        },
        component: () => import("../views/Admin/Home.vue"),
      },
      {
        path: "/Admin/UserPerson",
        name: "AdminUserPerson",
        meta: {
          title: "账号信息",
          isAdmin: true,
        },
        component: () => import("../views/Admin/UserPerson.vue"),
      },
      {
        path: "/Admin/PasswordEdit",
        name: "AdminPasswordEdit",
        meta: {
          title: "修改密码",
          isAdmin: true,
        },
        component: () => import("../views/Admin/PasswordEdit.vue"),
      },
      {
        path: "/Admin/UserList",
        name: "AdminUserList",
        meta: {
          title: "账号管理",
          isAdmin: true,
        },
        component: () => import("../views/Admin/UserList.vue"),
      },
      {
        path: "/Admin/admissionList",
        name: "AdmissionList",
        meta: {
          title: "出入院登记",
          isAdmin: true,
        },
        component: () => import("../views/Admin/admissionList.vue"),
      },
      {
        path: "/Admin/bedList",
        name: "BedList",
        meta: {
          title: "床位信息",
          isAdmin: true,
        },
        component: () => import("../views/Admin/bedList.vue"),
      },
      {
        path: "/Admin/bed_reservationList",
        name: "BedReservationList",
        meta: {
          title: "床位预约",
          isAdmin: true,
        },
        component: () => import("../views/Admin/bed_reservationList.vue"),
      },
      {
        path: "/Admin/notificationList",
        name: "AdminNotificationList",
        meta: {
          title: "系统通知",
          isAdmin: true,
        },
        component: () => import("../views/Admin/notificationList.vue"),
      },
      {
        path: "/Admin/permissionList",
        name: "PermissionList",
        meta: {
          title: "权限信息",
          isAdmin: true,
        },
        component: () => import("../views/Admin/permissionList.vue"),
      },
      {
        path: "/Admin/role_permissionList",
        name: "RolePermissionList",
        meta: {
          title: "角色与权限关联",
          isAdmin: true,
        },
        component: () => import("../views/Admin/role_permissionList.vue"),
      },
      {
        path: "/Admin/userList",
        name: "UserInfoList",
        meta: {
          title: "用户信息",
          isAdmin: true,
        },
        component: () => import("../views/Admin/UserList.vue"),
      },
      {
        path: "/Admin/wardList",
        name: "WardList",
        meta: {
          title: "病区信息",
          isAdmin: true,
        },
        component: () => import("../views/Admin/wardList.vue"),
      },
      {
        path: "/Admin/bedPoolList",
        name: "BedPoolList",
        meta: {
          title: "床位池查看",
          isAdmin: true,
        },
        component: () => import("../views/Admin/bedPoolList.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    ...publicRoutes,
    ...frontRoutes,
    ...adminRoutes,
    {
      path: "/:pathMatch(.*)*",
      name: "NotFound",
      component: () => import("../views/NotFound.vue"),
    },
  ],
});

router.beforeEach(async (to, from, next) => {
  NProgress.start();

  const commonStore = useCommonStore();

  if (!commonStore.Token) {
    if ((to.meta && to.meta.isAdmin) || (to.meta && to.meta.requiresAuth)) {
      next({ path: "/Login" });
    } else {
      next();
    }
    return;
  }

  if (!commonStore.HasUserInfo) {
    await commonStore.GetInfo();
    if (!commonStore.UserId) {
      await commonStore.Logout();
      next({ path: "/Login" });
      return;
    }
  }

  const isAdminUser = commonStore.UserInfo && commonStore.UserInfo.RoleType === 1;

  if (to.path === "/Login" || to.path === "/") {
    next({ path: isAdminUser ? "/Admin" : "/Front/Reservation" });
    return;
  }

  if (to.meta && to.meta.isAdmin) {
    if (isAdminUser) {
      next();
    } else {
      next({ path: "/Front/Reservation" });
    }
    return;
  }

  if (isAdminUser && to.path.startsWith("/Front")) {
    next({ path: "/Admin" });
    return;
  }

  next();
});

router.afterEach(() => {
  window.scrollTo({ top: 0 });
  NProgress.done();
});

export default router;
