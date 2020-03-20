<template>
    <div>
        <div class="square" v-if="currentDesk && currentDesk.start">
            <div class="container-fluid">
                <h2>Your current desk</h2>
                <DeskItem v-bind:item="currentDesk"> </DeskItem>
            </div>
        </div>

        <div>
            <Stats></Stats>
        </div>

        <div v-if="sittings && sittings.length>0">
            <h2>Recent activities</h2>
            <LastActivity v-bind:sittings="lastSittings"></LastActivity>
        </div>

        <div class="content centered">
        <h2> Monitor page</h2>
        <p>We stream the workspace utilisation here ...</p>
        <img class="ui small image centered" src="../assets/Panneau_travaux.svg.png"/>
        </div>

    </div>
</template>

<script>
    import siteService from "../service/SiteService";
    import DeskItem from "./DeskItem";
    import LastActivity from "./LastActivity";
    import Stats from "./Stats";
    export default {
        name: "Admin",
        components: {LastActivity, DeskItem,Stats},
        data(){
            return{
                currentDesk:null,
                lastDesk:null,
                displayCurrentDesk:false,
                sittings:[],
                lastSittings:[]
            }
        },
        created(){
            siteService.getProfile().then(resp =>{
                this.sittings = resp.data.sittings;
                const closedSit = (sitting) => sitting && sitting.start!==null && sitting.end!==null;
                //get closed sits
                this.lastSittings= this.sittings.filter(closedSit);
                //
             let idxCur = this.sittings.findIndex(sit => sit.end === null && sit.start);
               if (idxCur !==-1){
                 this.currentDesk = this.sittings[idxCur];
              }
            this.lastSittings.sort((a, b) => a.start.localeCompare(b.start));
            console.log(`${JSON.stringify(this.currentDesk)}`);

            });

        }
    }
</script>

<style scoped>

</style>