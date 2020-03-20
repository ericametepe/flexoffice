<template>
    <div id="prefForm">
        <form>
            <p>Make your choice </p>
        <select v-model="formData.siteId" v-on:change="fillFloors()">
            <option selected disabled hidden style='display: none' value=''></option>
            <option selected="selected" value="-1">--- Your site in th list  ---</option>
            <option v-for="site in optionSites" v-bind:key="site.id" v-bind:value="site.id">{{site.name}}</option>
        </select>

            <select v-model="formData.floorId" v-on:change="fillDesks()">
            <option >--- The floor in th list  --- </option>
            <option v-for="floor in floors" v-bind:key="floor.id" v-bind:value="floor.id">{{floor.id}}</option>
        </select>

            <select v-model="formData.deskId">
            <option>--- Your desk  --- </option>
            <option v-for="desk in desks" v-bind:key="desk.id" v-bind:value="desk.id">{{desk.desknumber}}</option>
        </select>

            <div class="ui blue button"  @click="registerPref()">
                <i class="heart icon" >Go</i>
            </div>


        </form>

    </div>

    
</template>

<script>
    import siteService from "../service/SiteService";
    import { eventBus } from '../main'

    export default {

        name: "PrefRegister",
        props:['userIp','prefs','sites'],
        data(){
            return {
                formData:{
                    "siteId":'',
                    "floorId":'',
                    "deskId":''

                },
                updateData : {
                    "userIp":'',
                    "favorite": {
                        "userIp": ''
                    }
                },
                optionSites:[],

                floors:[],

                desks:[],

                prefItem :{
                "deskId": '',
                "deskNumber": '',
                "deskStatus": '',
                "siteName": '',
                "floorId": '',
                "siteId": '',
                "userIp": ''
                }
          }
        },

        created(){
            this.optionSites=this.sites;
        },

        methods:{
            registerPref(){
                this.updateData.favorite.userIp=this.userIp;
                this.updateData.userIp=this.userIp;



                siteService.updateDesk(this.formData.siteId, this.formData.floorId, this.formData.deskId, this.updateData)
                    .then(resp => {

                        //search the prefItem
                        if (resp.data) {
                            let idx = this.sites.findIndex(site => site.id.localeCompare(this.formData.siteId) === 0);

                            if (idx !== -1) {
                                let site = this.sites[idx];
                                let idx2 = site.floors.findIndex(floor => floor.id.localeCompare(this.formData.floorId) === 0);
                                if (idx2 !== -1) {
                                    let floor = site.floors[idx2];
                                    let idx3 = floor.desks.findIndex(desk => desk.id.localeCompare(this.formData.deskId) === 0);
                                    if (idx3 != -1) {
                                        let desk = floor.desks[idx3];
                                        this.prefItem.siteId = this.formData.siteId;
                                        this.prefItem.siteName = site.name;
                                        this.prefItem.floorId = this.formData.floorId;
                                        this.prefItem.deskId = this.formData.deskId;
                                        this.prefItem.deskNumber = desk.desknumber;
                                        this.prefItem.deskStatus = desk.state;

                                        this.prefs.push(this.prefItem);

                                    }
                                }
                            }

                            this.prefItem = {};
                            this.formData = {};
                            this.fillSites();
                            eventBus.$emit('addPref', this.prefItem);
                        }

                    }).catch(error => {
                        console.error(`Fail cause of  : ${JSON.stringify(error)})}`);
                });


            },

            closePref(){
                eventBus.$emit('closePref',this.prefItem);
            },

            fillSites(){
                console.log(`optionSites : ${JSON.stringify(this.optionSites)}`);
                this.optionSites.forEach((site, sidx)=>{

                    if (!site.floors || site.floors.length===0){
                        this.optionSites.splice(sidx,1);
                    }
                    //update site
                    site.floors.forEach((floor,index)=>{
                        if (floor.desks && floor.desks.length===0){

                            site.floors.splice(index,1);
                        }

                        let allDesks = this.getAllDeskFromFloor(floor);
                        if (!allDesks || allDesks.length===0 || allDesks.every(desk => this.prefs.findIndex(pref=>pref.deskId.localeCompare(desk.id)===0)!==-1)){
                            this.optionSites[sidx].floors.splice(index,1);
                        }
                    });

                    let allDesks = this.getAllDeskFromSite(site);
                    if (!allDesks || allDesks.length===0 || allDesks.every(desk => this.prefs.findIndex(pref=>pref.deskId.localeCompare(desk.id)===0)!==-1)){
                        this.optionSites.splice(sidx,1);
                    }


                });
                },


            getAllDeskFromFloor(f){
                let allDesk=[];
                if (f && f.desks && f.desks.length>0){
                f.desks.forEach(desk => {
                    if (desk && desk!==null){
                      allDesk.push(desk);
                    }
                });
                }
                return allDesk;
            },


            getAllDeskFromSite(site){
                let allDesk=[];
                site.floors.forEach(f => {
                    if (f.desks && f.desks.length>0){
                        allDesk.push(f.desks);
                    }
                });
                return allDesk;
            },


            fillFloors(){
                let idx = this.optionSites.findIndex(site => site.id.localeCompare(this.formData.siteId)===0);
                    this.floors=[];
                if (idx!==-1){
                    this.optionSites[idx].floors.forEach((floor, index)=>{

                        if (floor && floor.desks.length==0){
                            this.optionSites[idx].floors.splice(index,1);

                        }

                    let allDesks = this.getAllDeskFromFloor(floor);

                    if (allDesks.length===0 || allDesks.length>0 && allDesks.every(desk => this.prefs.findIndex(pref=>pref.deskId.localeCompare(desk.id)===0)!==-1)){
                        this.optionSites[idx].floors.splice(index,1);
                    }

                });
                     this.floors = this.optionSites[idx].floors;


                }
            },
            fillDesks(){
                let idx = this.floors.findIndex(floor => floor.id.localeCompare(this.formData.floorId)===0);

                if (idx!==-1){
                    this.desks = this.floors[idx].desks;


                    this.desks.forEach((desk, index) => {
                        if (this.prefs.findIndex(pref => pref.deskId.localeCompare(desk.id)===0) !==-1){
                            this.desks.splice(index,1);
                        }
                    });

                }
            }
        }


    }
</script>

<style scoped>

</style>
