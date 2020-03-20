<template>
    <div>
    <div class="ui blue button"  v-on:click="freeDesk(item)" v-if="inuse">
        <i class="chevron circle right" >Leave</i>
    </div>

    <div class="ui blue button"  v-on:click="useDesk(item)" v-if="!inuse">
        <i class="chevron circle right" >Leave</i>
    </div>
    </div>
    
</template>

<script>
    import siteService from "../service/SiteService";
    import userProfile from "../service/UserProfileService";

    export default {
        name: "FreeDeskAction",
        props:['item'],

        data(){
          return{
              updateData : {
                  "userIp":null,
                  "state" : null
              },
              inuse:true
        }
        },

        methods:{
            async freeDesk(item){
                console.log(`Call it men ${JSON.stringify(item)} `);
                let userIp = await userProfile.getUserIpInfo();

                let normalIp = userIp.ip.split('.').join('-');
                this.updateData.userIp=normalIp;
                this.updateData.state='FREE';


                siteService.updateDesk(item.siteId, item.floorId, item.deskId, this.updateData)
                    .then(resp => {
                        console.log(`ok use the desk as public property...${JSON.stringify(resp.data)}`);
                    });
            },

            async useDesk(item){

                let userIp = await userProfile.getUserIpInfo();

                let normalIp = userIp.ip.split('.').join('-');

                this.updateData.userIp=normalIp;
                this.updateData.state='OCCUPIED';

                siteService.updateDesk(item.siteId, item.floorId, item.deskId, this.updateData)
                    .then(resp => {
                        this.inuse=false;
                        console.log(`ok use the desk as public property...${JSON.stringify(resp.data)}`);
                    }).catch(reason => {
                    if (reason.response.status === 400) {
                        this.inuse = true;
                        alert(`${JSON.stringify(reason.response.data.detail)}`);
                        console.error(`${JSON.stringify(reason)}`);
                        //
                    }
                });

            }

        }

    }
</script>

<style scoped>

</style>