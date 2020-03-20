<template>
    <div name="not" @mouseleave="closeNotItems" @mouseover="displayItem()">
            <div class="ui red circular label">
        <i class="icon bell outline" >
            <span v-if="evtCounter>0">
            {{evtCounter}}
            </span>
        </i>
            </div>
        <div class="container-fluid"  name="listNot" v-if="displayIt && notifs && notifs.length>0">
            <ul class="list-group">
            <li class="list-group-item" v-for="(not, idx) in notifs" v-bind:key="idx" v-bind:class="{'list-group-item-primary':idx%2===0}">{{not.type +'on desk '+not.desk.desknumber}} </li>
           </ul>
        </div>
    </div>
    
</template>

<script>
    import { eventBus } from "../main";
    export default {
        name: "Notification",
        data(){
            return{
            evtCounter:0,
                displayIt:false,
                notifs:[]
            }
        },
        mounted(){

            eventBus.$on('saveReport',(desk)=>
            {
                this.evtCounter++;
                this.addNotif('saveReport', desk,`your report on desk : ${desk.desknumber}`);
            });

            eventBus.$on('useDesk',(desk)=>
            {
                this.evtCounter++;
                this.addNotif('useDesk', desk, `your last desk in use : ${desk.desknumber}`);

            });
        },
        methods:{
            displayItem(){
                this.displayIt=true;
            },
            addNotif(type, desk, message){
                let notif = {};
                notif.type=type;
                notif.desk=desk;
                notif.message=message;
                this.notifs.unshift(notif);
            },
            closeNotItems(){
              this.displayIt=false;
              this.notifs.shift();
              this.evtCounter=0;
            }

        }
    }
</script>

<style scoped>

</style>