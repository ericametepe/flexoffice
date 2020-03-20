<template>
    <div class="center container-lg">
        <img  class="ui large image" id="ada1" :src="ada_lovelace_url" alt="No img yet">
    </div>
</template>

<script>
    import siteService from "../service/SiteService";

    export default {
        name: "QRCode",
        props:['deskItem'],

        data(){
            return{
                ada_lovelace_url:null
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
                siteService.loadImage(this.deskItem).then( (response) =>
                    this.ada_lovelace_url = window.URL.createObjectURL(new Blob([response.data]))
                );
            }

        }
    }
</script>

<style scoped>

</style>
