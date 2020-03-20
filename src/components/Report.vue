<template>
    <div class="content centered">
        <div class="content centered">
            <label> Report your problem</label>
            <form id="deskId" name="reportPb" >
                <select v-model="report.code">
                    <option v-for="rep in reportTypes" v-bind:key="rep.code" v-bind:value="rep.code"> {{rep.description}} </option>
                </select>
                <label>Report Description</label>
                <input type="text" v-model="report.description"/>
                <button type="button" class="btn btn-primary" @click="registerReport(report)"> Save</button>
                <span></span>
                <button type="reset" class="btn btn-dark" @click="closeReport"> Cancel</button>
            </form>
        </div>
    </div>
    
</template>

<script>
    import siteService from "../service/SiteService";
    import userProfile from "../service/UserProfileService";
    import { eventBus } from '../main'
    export default {
        name: "Report",
        props:['desk'],

        data(){
            return{
                reportTypes:[],
                report:{},
                display:false,
                localIndex:-1
          }
        },

    created(){
            this.reportTypes = siteService.getReportType();
        },



        methods:{
            async registerReport(){
                this.report.siteId=this.$route.params.siteId;
                this.report.floorId=this.$route.params.floorId;
                this.report.deskId=this.desk.id;

                let ipInfo = await userProfile.getUserIpInfo();


                if(ipInfo){
                    let currentUser =ipInfo.ip;
                    if (currentUser){
                        let ipNormal = currentUser.split('.').join('-');
                        this.report.userIp=ipNormal;
                    }
                }
                siteService.saveReport(this.report).then(resp=>{
                    console.log(`Statud : ${JSON.stringify(resp)}`);
                        alert('Your problem is reported with success');
                    this.report= {};
                    eventBus.$emit("saveReport",resp.data);
                }).catch(err=>{
                    console.log(`${err}`);
                });
            },

            closeReport(){
                eventBus.$emit('closeReport',event);
    }

        }

    }
</script>

<style scoped>

</style>