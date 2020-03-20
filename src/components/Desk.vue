<template>



    <div name="desk">


        <ul class="list-group">
            <li class="list-group-item" v-for="(desk, index) in computedDesk" v-bind:key="desk.id">

                <div name="qrcode">
                    <QRCode v-bind:desk-item="displayBarCode(desk.id)" v-if="scanDisplay && displayQrcodeItemId===index"></QRCode>
                </div>


                <div class="card-img">{{desk.desknumber}}</div>
                    <div class="ui blue button" v-if="desk.free" v-on:click="useDesk(desk)">
                        <i class="chevron circle right" >Sit</i>
                        <div v-if="useDeskError">
                            <AlertDeskInUse desklink="" warning="">
                            </AlertDeskInUse>
                        </div>

                    </div>

                    <div class="ui blue button" v-if="!desk.free" v-on:click="freeDesk(desk)">
                        <i class="chevron circle right" >Leave</i>
                    </div>

                    <div class="ui yellow button" @click="displayRate(index)">
                        <i class="star icon" ></i>
                    </div>


                <div class="ui black button" @click="displayQrcode(index)">
                    <i class="barcode icon">Scan</i>
                </div>




                <i class="ui basic pink left pointing label" v-if="desk.ratingCount &&desk.ratingCount>0"> {{desk.ratingCount}}</i>


                <div class="ui pink button" v-bind:class="{disabled:desk.favorite}" @click="addToFav(desk)">
                        <i class="heart icon" ></i>
                    </div>


                <span></span>
                <div  class="ui red button" @click="displayReport(index)">
                    <i class="flag icon">  </i>
                </div>
                <i class="ui basic red left pointing label" v-if="desk.countReport &&desk.countReport>0"> {{desk.countReport}}</i>


                <div class="ui label" v-if="!desk.free">
                    Duration use
                    <div class="detail">{{desk.useStartDate | dateFromArr}}</div>
                </div>

                 <report   v-bind:desk="desk"   v-if="displayR===index"></report>

                 <rating   v-bind:desk="desk"    v-if="displayRating===index"> </rating>




            </li>
        </ul>

    </div>
</template>

<script>
    import siteService from "../service/SiteService";
    import moment from "moment";
    import Report from "./Report";
    import { eventBus } from '../main';
    import Rating from "./Rating";
    import userProfile from "../service/UserProfileService";
    import localStorageService from "../service/LocalStorageService";
    import AlertDeskInUse from "./AlertDeskInUse";
    import QRCode from "./QRCode";

    export default {
        name: "Desk",
        components: {QRCode, AlertDeskInUse, Rating, Report},
        data(){
            return {
                desks:[],
                useDeskError:false,
                scanDisplay:false,
                valParams :{},
                displayR:null,
                displayRating:null,
                currentUser : '',
                displayQrcodeItemId:null,
                updateData : {
                    "favorite": {
                            "userIp": null
                    }
                },
            }
        },
        created(){
            this.valParams = this.$route.params;
            console.log(`ok use the desk as public property...${JSON.stringify(this.valParams.siteId)}`);
            this.fetchDesk(this.valParams.siteId, this.valParams.floorId);
            this.currentUser = this.getCurrentUser();
            },


        computed:{
             computedDesk:  function() {
                 let result =[];
                 let currentUser = null;

                 let userGeoloc = localStorageService.getFromLocal("userGeoloc");

                 if (userGeoloc){
                   currentUser = userGeoloc.ip;
                 }

                 console.log(`get currentUser : ${currentUser}`);


                 if (currentUser){
                 currentUser = currentUser.split(".").join("-");
                 this.desks.forEach(desk =>{
                     if (desk.favorites && desk.favorites.length >0){

                         let test = desk.favorites.some(fav =>{
                             return currentUser.localeCompare(fav.userIp)===0;
                         });

                         desk.favorite=test;

                     }
                     result.push(desk);
                 });
                 return result;
             }
             else {
             return this.desks;
             }
             }
        },

        mounted(){
          this.updateReport();
          this.updateRating();
          this.closeReport();
          this.closeRate();
        },


       methods:{

           getCurrentUser(){
               this.currentUser = userProfile.getUserIpInfo();
           },

           displayBarCode(deskId){
               let desk = new Object();
               desk.siteId= this.valParams.siteId;
               desk.floorId=this.valParams.floorId;
               desk.deskId=deskId;
               console.log(`Yes log me like that : ${JSON.stringify(desk)}`);
               return desk;
           },

           displayQrcode(index){
             this.scanDisplay=!this.scanDisplay;
             this.displayQrcodeItemId=index;
           },

           async calculateFavDesk(){
               let result =[];
               let  info = await  userProfile.getUserIpInfo();
               let currentUser = info.ip;

               console.log(`get currentUser : ${currentUser}`);

               this.desks.forEach(desk =>{
                   if (desk.favorites && desk.favorites.length >0){
                       let test = desk.favorites.some(fav =>{
                           fav.userIp === currentUser
                       });
                       desk.favorite=test;

                   }
                   result.push(desk);
               });
               return result;
           },

            updateReport(){
                eventBus.$on("saveReport",desk=>{
                    this.replaceDesk(desk);
                });

            },
           updateRating(){
               eventBus.$on("saveRating",desk=>{
                   this.replaceDesk(desk);
               });

           },

           closeReport(){
               eventBus.$on('closeReport',event=>{
                   if (event){
                       console.log(`yes we get : ${event}`);
                       this.displayR=-1;
                   }
               })

           },

           closeRate(){
               eventBus.$on('closeRating',event=>{
                   if (event){
                       console.log(`yes we get : ${event}`);
                       this.displayRating=-1;
                   }
               })

           },


           async useDesk(desk){

                let userIp = await userProfile.getUserIpInfo();

                let normalIp = userIp.ip.split('.').join('-');

                this.updateData.userIp=normalIp;
                this.updateData.state='OCCUPIED';

                siteService.updateDesk(this.valParams.siteId, this.valParams.floorId, desk.id,this.updateData)
                    .then(value => {
                        console.log(`ok use the desk as public property...${JSON.stringify(this.updateData)}`);
                        eventBus.$emit('useDesk',desk);
                        this.replaceDesk(value.data);
                    }).catch(reason => {
                        if (reason.response.status === 400) {
                            this.useDeskError = true;
                            alert(`${JSON.stringify(reason.response.data.detail)}`);
                            console.error(`${JSON.stringify(reason)}`);
                            //
                        }
                });

           },

           replaceDesk(desk) {
               let idx = this.desks.findIndex(elt => elt.id === desk.id);
               this.desks.splice(idx, 1, desk);
           },

            async freeDesk(desk){
                let userIp = await userProfile.getUserIpInfo();

                let normalIp = userIp.ip.split('.').join('-');

                this.updateData.userIp=normalIp;
                this.updateData.state='FREE';

                siteService.updateDesk(this.valParams.siteId, this.valParams.floorId, desk.id,this.updateData)
                    .then(value => {
                        console.log(`ok use the desk as public property...${JSON.stringify(this.updateData)}`);
                        this.replaceDesk(value.data);
                    });

            },

            async addToFav(desk){
                let userIp = await userProfile.getUserIpInfo();

                let normalIp = userIp.ip.split('.').join('-');

                this.updateData.favorite.userIp=normalIp;
                this.updateData.userIp=normalIp;

                siteService.updateDesk(this.valParams.siteId, this.valParams.floorId, desk.id, this.updateData)
                    .then(resp => {
                        this.replaceDesk(resp.data);
                        //store to  the prefs
                        console.log(`Ok u go : ${JSON.stringify(resp.data)}`);

                    }).catch(error => {
                        console.log(error);
                        alert('U exceed the max favs');
                    }
                ).finally(fin  =>console.log(`on finish ${JSON.stringify(fin)}`));

            },

           displayReport: function(index){
                   this.displayR=index;
           },

           displayRate(index){
               this.displayRating=index;

           },


            fetchDesk(siteId, floorId){
                siteService.getDesksBySiteIddAndFid(siteId, floorId).then(result => this.desks=result.data);
            },

            refreshDesk(){
                this.valParams = this.$route.params;
                console.log(`ok use the desk as public property...${JSON.stringify(this.valParams)}`);
                this.fetchDesk(this.valParams.siteId, this.valParams.floorId);

            }
        },
        filters:{
            duration:function (seconds) {
                moment.duration(seconds,'seconds');

            },
            dateFromArr(dateArray){
                let now = new moment();
                let then = new moment(dateArray);
                return moment.utc(moment(now,"yyyy-MM-dd HH:mm:ss").diff(moment(then,"yyyy-MM-dd HH:mm:ss"))).format("HH:mm:ss");



            }
        }
    }
</script>

<style scoped>
    [v-cloak] {
        display:none;
    }

</style>
