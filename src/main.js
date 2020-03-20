import Vue from 'vue';
import App from './App.vue';
import router from './router';
import "bootstrap/dist/css/bootstrap.min.css";
import "semantic-ui-css/semantic.min.css";
import SuiVue from 'semantic-ui-vue';

Vue.config.productionTip = false

Vue.use(SuiVue);

export const eventBus = new Vue();



// noinspection JSAnnotator
new Vue({
    render: h => h(App),
    router,
    data:{
    }
}).$mount('#app')

