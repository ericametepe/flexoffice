<template>
    <div class="ui icon input">
        <input type="text" placeholder="Site, floor..." v-model="key"/>
        <i class="search link icon" v-on:click="find(key)"></i>
    </div>
</template>
<script>
    import siteService from "../../service/SiteService";
    import {eventBus} from "../../main";

    export default {
        name: 'Search',
        data(){
            return{
                key:'',
                result:[]
            }
        },

        methods:{
            find(key){
                siteService.search(key).then(resp =>{
                    this.result= resp.data;
                    let sresult = {
                        queryTerm:key,
                        "sites":this.result
                    };
                    eventBus.$emit("search-site",sresult);
                    console.log(`${key} give response :${JSON.stringify(this.result)}`);
                    this.key='';
                })
            }

        }
    }
</script>
