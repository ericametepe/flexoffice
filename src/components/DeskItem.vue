<template>
    <div class="content">

        <div class=" center container-lg" v-if="scanDisplay" >
            <img  class="ui large image" id="ada1" :src="ada_lovelace_url" alt="No img yet" @mouseout="scanDisplay=false">
        </div>


        <div class="ui label">
            <i class="desktop icon"> Desk</i>
            {{item.deskNumber}}
        </div>


        <div class="ui label">
            <i class="level up alternate icon"> Floor</i>
            {{item.floorId}}
        </div>
        <div class="ui label">
            <i class="building icon">Site</i>
            {{item.siteName}}

        </div>  <div class="ui label" @mouseover="scanDisplay=true">
            <i class="barcode icon">Scan</i>
        </div>



    </div>
    
</template>

<script>
    import siteService from "../service/SiteService";
    export default {
        name: "DeskItem",
        props:['item'],
        data(){
            return{
              ada_lovelace_url:null,
                scanDisplay:false
            }
        },
        created(){
            console.log(" item value from parent :" + JSON.stringify(this.item));

        },

        mounted(){
            this.loadBarcode();
        },

        methods:{
            loadBarcode(){
            let desk = this.item;
                siteService.loadImage(desk).then( (response) =>
                this.ada_lovelace_url = window.URL.createObjectURL(new Blob([response.data]))
                );
            }

        },
    }
</script>

<style scoped>

</style>