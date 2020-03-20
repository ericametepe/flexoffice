<template>
<div name="listing">


    <div name="sresult" class="ui list" v-if="searchResult &&searchResult.length>0">
        <h2 v-text="'Result for : ' +queryTerm"> </h2>
        <div class="item"  v-for="site in sites" :key="site.id">
            <div class="content">

                <a class="ui link header" v-on:click="displayFloors(site)">
                    <i class="map marker icon"></i>
                    {{site.name}}
                </a>
                <router-link to="/floor"> Floor</router-link>
                <div class="description">
                    {{site.addressLabel}}
                </div>
                <div class="description">
                    {{site.freeDeskCount}}
                </div>
            </div>
        </div>
    </div>

    <div class="container-fluid">
        <h2> Liste des sites </h2>
        <div class="row" v-for="(chunk,index) in chunkedSites" v-bind:key="index">

        <div class="col-sm" v-for="site in chunk" v-bind:key="site.id">
                <a  @click="displayFloors(site)">
                    <i class="map marker icon blue" ></i>
                    <div class="description">
                    {{site.name}}
                    </div>
                </a>

                <div class="description">
                    {{site.addressLabel}}
                </div>
                <div class="badge-pill">
                    {{site.freeDeskCount}}
                </div>
            </div>
    </div>

    </div>




</div>
    
</template>

<script>
    import siteService from "../service/SiteService";
    import router from "../router";
    import { eventBus } from '../main';
    import localService from "../service/LocalStorageService";
    import ipService from "../service/IpInfoService";



    export default {
        name: "Site",
        data(){
            return {
                sites : [],
                queryTerm: '',
                searchResult:[],
                nbPerRow : 3,
                chunkedSites:[]
            }
        },

        created(){
        siteService.getSites()
        .then(result => {
             this.chunkedSites=result.data;
            });

            //this.initUser();
        },

        methods:{
            async initUser(){
                let userIp = localService.getFromLocal("userGeoloc");

                if(!userIp || userIp===null){
                    await ipService.getIpInfo().then(resp=>{
                        localService.saveToLocal("userGeoloc",resp.data);
                    });
                }

            },


            displayFloors(site){
              router.push(`/sites/${site.id}/floors`);
            eventBus.$emit("display-site",site);
            },

            retrieveSearch(){
                eventBus.$on("search-site",sresult=>{
                    this.queryTerm = sresult.queryTerm;
                    this.searchResult=sresult.sites;
                    console.log(`receiver ${JSON.stringify(sresult)}`);
                });

            }

        },

        computed:{


        },

        mounted(){
            this.retrieveSearch();
        }



    }
</script>

<style scoped>

</style>
