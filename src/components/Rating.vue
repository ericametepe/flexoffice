<template>

    <div class="content centered">
        <div class="content centered">
            <label> Rate your office</label>
            <form id="rateId" name="rating" >
                <star-rating text-class="custom-text" v-model="updateData.rating.stars"></star-rating>
                <label>Your comment</label>
                <input type="text" v-model="updateData.rating.description"/>
                <button type="button" class="btn btn-primary" @click="registerRate()"> Save</button>
                <span></span>
                <button type="reset" class="btn btn-dark" @click="closeRating"> Cancel</button>
            </form>
        </div>
    </div>
    
</template>

<script>
    import StarRating from 'vue-star-rating'
    import {eventBus} from "../main";
    import siteService from "../service/SiteService";
    import userProfile from "../service/UserProfileService";



    export default {
        props:['desk'],
        name: "Rating",
        components:{StarRating},

        data(){
            return{
            updateData : {
                "rating": {
                    "stars": 0,
                    "description": '',
                    "userIp": ''
            }
            },
            }
        },



        methods:{
            async registerRate(){
                let siteId=this.$route.params.siteId;
                let floorId=this.$route.params.floorId;
                let deskId=this.desk.id;

                let userIp = await userProfile.getUserIpInfo();

                let normalIp = userIp.ip.split('.').join('-');

                this.updateData.rating.userIp=normalIp;
                this.updateData.userIp=normalIp;

                siteService.updateDesk(siteId,floorId,deskId, this.updateData).then(resp=>{
                    console.log(`Response fromupdate : ${JSON.stringify(resp)}`);
                    //alert('Your rating is saved with success');
                    eventBus.$emit("saveRating",resp.data);
                    this.updateData.rating.stars=0;
                    this.updateData.rating.description='';


                }).catch(err=>{
                    console.log(`${err}`);
                });
            },

            closeRating(){
                eventBus.$emit('closeRating',event);
            },


        }

    }
</script>

<style scoped>
    .vue-star-rating {
      display:inline-block
    }
</style>