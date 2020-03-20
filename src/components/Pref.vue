<template>




    <div name="pref">

        <div>
            <h2>Preferences</h2>
            <sui-tab :menu="{ pointing: true }">


                <sui-tab-pane title="Your favorite sites" :attached="false">
                    <div name="vtable">
                        <table class="ui celled table"  v-if="localPrefs && localPrefs.length>0">
                            <thead>
                            <tr>
                                <th>Site</th>
                                <th>Floor</th>
                                <th>Desk</th>
                                <th>Desk status</th>
                                <th> </th>

                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="localPref in localPrefs" v-bind:key="localPref.deskId">
                                <td data-label="sName">{{localPref.siteName}}</td>
                                <td data-label="fName">{{localPref.floorId}}</td>
                                <td data-label="dName">{{ localPref.deskNumber}}</td>
                                <td data-label="staName">{{localPref.deskStatus}}</td>
                                <td data-label="actName">
                                    <span>
                        <button class="btn btn-danger" @click="deletePref(localPref)">
                            <i class="icon trash"></i>
                        </button></span></td>
                            </tr>
                            </tbody>
                        </table>

                        <div v-if="localPrefs && localPrefs.length===0">
                            <span> {{'No favorite yet '}} </span>
                            <i v-for="i in localPrefs.length" v-bind:key="i" class="icon pink  heart"> </i>
                            <span> {{'u can add at most '+(maxFav-localPrefs.length)}}&nbsp;</span>

                                <div class="ui button green" v-if="!displayFormStatus" @click="displayForm()">
                                <i   class="icon plus square outline pink"> </i>
                                </div>

                        </div>

                        <div v-if="localPrefs.length && localPrefs.length<maxFav">
                            <span> {{'You have '}} </span>
                            <i v-for="i in localPrefs.length" v-bind:key="i" class="icon pink  heart"> </i>
                            <span> {{'u can add at most '+(maxFav-localPrefs.length)}}&nbsp;</span>

                                <div class="ui button green" v-if="!displayFormStatus" @click="displayForm()">
                                <i   class="icon plus square outline pink"> </i>
                                </div>
                        </div>


                        <div v-if="localPrefs.length && localPrefs.length===maxFav">
                            <span> {{'You have '}} </span>
                            <i v-for="i in localPrefs.length" v-bind:key="i" class="icon pink  heart"> </i>
                            <span> Enjoy it </span>
                        </div>

                        <div v-if="displayFormStatus">
                            <PrefRegister v-bind:user-ip="userPref" v-bind:prefs="localPrefs" v-bind:sites="sites"></PrefRegister>
                        </div>
                    </div>

                </sui-tab-pane>
                <sui-tab-pane title="Recommendations" :attached="false">
                    <div name="location">
                        <div name="favloc">
                            <div name="stpoint" class="content" v-if="profile.adresse">
                            <label> Favorite departure point :  </label>
                            <span  class="badge badge-success">
                                {{profile.adresse}}
                            </span>
                                <button class="btn-info" @click="displayLocationForm()">
                                        <i class="icon edit"></i>
                                </button>

                                <div name="formAdr" v-if="locationForm">
                                    <form>
                                        <label>Your departure point adresse</label>
                                        <input type="text" v-model="profile.adresse"/>
                                        <button type="button" class="btn-info" @click="saveAdresse(profile)"> Save</button>
                                        <button type="reset" class="btn-dark"  @click="closeAdrForm()"> Cancel</button>
                                    </form>
                                </div>

                            </div>

                                <div name="norecoTable" v-if="localRecos && localRecos.length==0">
                                    <label>No recommendations</label>

                                    <div name="noadr">
                                        <label>{{'Your current geolocalisation adresse :'+JSON.stringify(geoadresse)}}</label>

                                        <form>
                                            <label>Your departure point adresse</label>
                                            <input type="text" v-model="profile.adresse"/>
                                            <button type="submit" class="btn-info" @click="saveAdresse(profile.adresse)"> Save</button>
                                            <button type="reset" class="btn-dark"  @click="closeAdrForm()"> Cancel</button>
                                        </form>
                                    </div>

                                </div>


                            <div name="recoTable" v-if="localRecos && localRecos.length>0">
                            <p> Our recommendations :  </p>
                                <table class="ui celled table"  >
                                    <thead>
                                    <tr>
                                        <th>Site</th>
                                        <th>Distance (km)</th>
                                        <th>Free desk</th>
                                        <th> </th>

                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr v-for="localReco in localRecos" v-bind:key="localReco.id">
                                        <td data-label="sName">
                                            <span class="badge">{{localReco.name+':'}}</span>
                                            <span class="badge-info">{{localReco.adress}}</span>
                                        </td>
                                        <td data-label="dName">{{ localReco.distance}}</td>
                                        <td data-label="staName">{{ localReco.totalfree}}</td>
                                        <td data-label="actName">
                                            <div class="btn btn-info" @click="gotoReco(localReco)"> Go
                                            <i class="icon location arrow"></i>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>



                            </div>


                    </div>

                </sui-tab-pane>


            </sui-tab>
        </div>


         </div>

</template>

<script>
    import siteService from "../service/SiteService";
    import geocodeService from "../service/GeocodeService";
    import localService from "../service/LocalStorageService";
    import ipService from "../service/IpInfoService";
    import userProfileService from "../service/UserProfileService";
    import {MAX_FAV} from "../service/SiteService";
    import PrefRegister from "./PrefRegister";
    import { eventBus } from '../main';




    export default {
        name: "Pref",
        components: {PrefRegister},
        data(){
            return{
                profile:{},
                ipinfo:{},
                geocode:{},
                currentPos:{},
                localPref:{},
                localPrefs:[],
                localRecos:[],
                newpref:{},
                sites:[],
                floors:[],
                desks :[],
                siteChoosen:{},
                floorChoosen:{},
                floorsSelect:[],
                deskSelect:[],
                segmentPrefs: [],
                displayFormStatus:false,
                locationForm:false,
                currentUser:'',
                geoadresse:{},
                userPref:'',
                optionSites:[],
                maxFav:0
            }
        },
        created(){
            siteService.getSites().then(response => {
                    let  all =response.data;
                    all.forEach((elt) => {
                      elt.forEach(e=>{
                        this.sites.push(e);
                      })

                    })
            });
            this.retrievePref();
            this.displayPref();
            this.getCurrentPosition();
            this.getRecos();
            this.getGeoadresse();
            this.updatePref();
            this.maxFav=MAX_FAV;
            this.cancelPref();


        },
        methods:{

            closeAdrForm(){
              this.locationForm=false;
            },

            gotoReco(localReco){
              this.$router.push(`/sites/${localReco.id}/floors`);

            },
            cancelPref(){
                eventBus.$on('closePref',()=>{
                    this.displayFormStatus=false;
                });

            },

            updatePref(){
                eventBus.$on("addPref",pref=>{
                    if(pref){
                    this.displayFormStatus=false;
                    }
                });

            },

            async getCurrentUser(){
                let userGeo = await userProfileService.getUserIpInfo();
                this.currentUser = userGeo.ip;
            },

            async getGeoadresse(){
                await (this.getCurrentPosition());

                //reverse geocode
                if (this.currentPos && this.currentPos.lat && this.currentPos.lng){
                geocodeService.getAdress(this.currentPos).then(resp =>{
                    this.geoadresse = resp.data.results[0].formatted;

                });
                }

                else{
                ipService.getIpInfo().then(resp =>{
                    this.geoadresse = resp.data;
                });

                }

            },

            async getRecos(){

                let userInfo = await userProfileService.getUserIpInfo();

                this.profile.adresse = `${userInfo.postal}, ${userInfo.city} ,${userInfo.country}`;

                let geoloc = userInfo.loc.split(',');
                this.currentPos.lat = geoloc[0];
                this.currentPos.lng = geoloc[1];

                if (this.currentPos && this.currentPos.lng && this.currentPos.lat) {
                    console.log(` current position for recos : ${JSON.stringify(this.currentPos)}`);
                siteService.getCommendation(this.currentPos).then(resp =>{
                    this.localRecos = resp.data;
                    }
                )
                }
            },

            async saveAdresse(address){
              // let userprof = await localService.getFromLocal("profile");

               //if (userprof  &&  userprof.adresse.localeCompare(this.profile.adresse)!==0){
                // await geocode service to get lng lat from adresse

                geocodeService.getGoogleGeocodeFromAdresse(address).then(resp => {
                    console.log(`response : ${JSON.stringify(resp.data)}`);

                    this.profile.geocode = resp.data.results[0].geometry;
                    this.profile.adresse = address;

                    localService.saveToLocal("profile",this.profile);
                    console.log(`Reverse geocode from ${address} ===> ${JSON.stringify(this.profile)}`);
                    this.profile.address='';
                    this.locationForm=false;

                }).catch(err => {
                    console.error(`Big REST problem : ${err}`);
                    alert(`${address} :  not valid follow the rule `);
                });

            },

            async saveGeolocation(){
                await (this.getCurrentPosition());

                  this.profile.geoloc = this.currentPos;
                  console.log(`Profile to save : ${JSON.stringify(this.profile)}`);
                  localStorage.removeItem("profile");
                  localStorage.setItem("profile",JSON.stringify(this.profile));

            },



            async getCurrentPosition(){
                if (navigator.geolocation){
                    navigator.geolocation.getCurrentPosition(this.initLocationInfo);
                }
                //no geoloc activated rely user ip & geoloc
                else {
                    let userInfo = await userProfileService.getUserIpInfo();
                    let geoloc = userInfo.loc.split(',');
                    this.currentPos.lat = geoloc[0];
                    this.currentPos.lng = geoloc[1];

                }

            },

            initLocationInfo(position){
                const lng = position.coords.longitude;
                const lat = position.coords.latitude;

                this.currentPos.lng = lng;
                this.currentPos.lat = lat;
                console.log(`longitude: ${ lng } | latitude: ${ lat }`);
                console.log(`Your current position :${JSON.stringify(this.currentPos)}`);
            },

            isadrloc(){
                return this.profile.loc.localeCompare("adrloc")== 0;
            },

            isgeoloc(){
                return this.profile.loc.localeCompare("geoloc")== 0;
            },



            async retrievePref(){
                //retrieve from db ...
                let user= await userProfileService.getUserIpInfo();

                if (user){
                    siteService.getProfile()
                        .then(resp=> {
                                this.localPrefs = resp.data.favoris;
                                this.userPref = resp.data.userIp;
                            }
                        )
                        .catch(reason => {
                            console.error(`Error : ${JSON.stringify(reason)}`);
                        });
                }

            },

            findSite(siteId){
                return this.sites.find(site => site.id.localeCompare(siteId)===0);
            },

            displayForm(){
              this.displayFormStatus=true;
            },
            displayLocationForm(){
              this.locationForm=true;
            },

            findFloor(siteId, floorId){
               return   this.findSite(siteId).floors.find(floor => {
                    floor.id.localeCompare(floorId)===0;
                });
            },

            deletePref(pref){
             siteService.deleteFav(pref).then(resp =>{
                 console.log(`delete fav ${JSON.stringify(resp)}`);

                 let idx = this.localPrefs.findIndex(value => value.deskId === pref.deskId);
                 this.localPrefs.splice(idx,1);
                 })
             .catch(reason => console.error(`things go wrong : ${JSON.stringify(reason)}`));

            },

            displayPref(){
                if(this.localPrefs){
                    this.localPrefs.forEach(localPref => {
                        console.log("localPref "+ JSON.stringify(localPref));
                 siteService.getDesk(localPref.siteChoosen,localPref.floorChoosen, localPref.deskChoosen)
                     .then(response => {
                        let desk = response.data;
                       console.log(`desk : ${JSON.stringify(desk)}`);

                       let   segmentPref ={};

                       segmentPref.desknumber = desk.desknumber;
                       segmentPref.deskId = desk.id;
                       segmentPref.status   = desk.state;

                       let site = {};


                       let idx = this.sites.findIndex(value => value.id===localPref.siteChoosen);
                       if(idx == ! -1){
                            site = this.sites[idx];
                       }
                       segmentPref.siteId = site.id;
                       segmentPref.siteName = site.name;

                       let floor = this.sites[idx].floors.find(value => value.id===localPref.floorChoosen);

                       segmentPref.floorId=floor.id;
                       segmentPref.floorName=floor.name;

                       console.log("Segment pref =====> "+JSON.stringify(segmentPref));
                      this.segmentPrefs.push(segmentPref);

                     });
             })
            }
        }
        },
        mounted(){
            this.updatePref();
        }

    }

</script>

<style scoped>

</style>