<template>
    <div name="listf">
        <div class="ui list">
            <p>{{'Site : '+ site.name}}</p>
            <h2 v-if="freeFloors && freeFloors.length>0"> Site details</h2>
            <div class="item" v-for="floor in freeFloors" :key="floor.id">
                <div class="content">
                    <a v-on:click="displayFloorDesk(floor.id)" class="header">
                        <i class="map marker icon"></i>
                        {{floor.name}}
                    </a>
                    <div class="description">
                        {{floor.freeDesk.length}}
                    </div>
                </div>
                </div>

        </div>

    </div>
</template>

<script>
    import siteService from "../service/SiteService";
    import router from "../router";



    export default {
        name: "Floor",
        data:function () {
            return {
                site:{},
                freeFloors: []
            }
        },



        created(){
           siteService.getSiteById(this.$route.params.siteId).then(result => {
               this.site=result.data;
               this.freeFloors=this.site.floors.filter(floor => (floor.freeDesk && floor.freeDesk.length>0) );
               }
           );

        },
        methods:{
            displayFloorDesk(floorId){
                router.push(`/sites/${this.site.id}/floors/${floorId}/desks`);
                //this.eventBus.$emit('displayF',site);
               // this.freeFloors.find(floor => floor.id==floorId);
            }

        },

    }
</script>

<style scoped>

</style>