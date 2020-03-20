import Vue from "vue";
import Site from "../components/Site";
import Floor from "../components/SiteItem";
import Desk from "../components/Desk";
import Pref from "../components/Pref";
import Admin from "../components/Admin";
import DeskItem from "../components/DeskItem";
import VueRouter from "vue-router";

Vue.use(VueRouter);

export default new VueRouter ({
    mode: "history",
    routes: [
        { name: "sites" ,path: "/", component: Site,alias:"site-listing"},
        { name: "floors",path: "/sites/:siteId/floors", component: Floor, alias:"siteFloors"},
        { name: "desks" ,path: "/sites/:siteId/floors/:floorId/desks", component: Desk,alias:"site-desk"},
        { name: "deskUnit",path: "/sites/:siteId/floors/:floorId/desks/:deskId", component: DeskItem},
        { name: "pref",path: "/pref", component: Pref,alias:"pref"},
        { name: "admin",path: "/admin", component: Admin,alias:"admin"},
        { path: "*",  redirect: "/" }
    ]
})

