import Vue from "vue";
import Vuex from "vuex";
import Axios from "axios";

Vue.use(vuex);

export default new Vuex.store({
    state: {
        sites:[],
        selectedSite:null
    },

    mutations: {
    selectSite(currentState, site){
        currentState.selectedSite=site;
    }

    }

})