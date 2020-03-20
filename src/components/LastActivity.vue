<template>
    <div name="lsit" v-if="sittings && sittings.length>0">
        <table class="ui celled table">
            <thead>
            <tr>
                <th>Desk</th>
                <th>Start Time</th>
                <th>Leave Time</th>
                <th>Duration (Hours)</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(sit, idx) in sittings" v-bind:key="idx">
                <td>{{sit.desknumber}}</td>
                <td>{{sit.start}}</td>
                <td>{{sit.end}}</td>
                <td>{{sit.duration| durationDisplay}}</td>
            </tr>
            </tbody>
            </table>
    </div>
    
</template>

<script>
    import moment from "moment";
    export default {
        name: "LastActivity",
        props:['sittings'],
        filters:{
            durationDisplay(x){
                let d = moment.duration(x, 'milliseconds');
                let hours = Math.floor(d.asHours());
                let mins = Math.floor(d.asMinutes()) - hours * 60;
                let secs = Math.floor(d.asSeconds()) - mins  *  60;
                return ` ${hours} : ${mins} : ${secs}`;

            }
        }
    }
</script>

<style scoped>

</style>