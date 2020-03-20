<template>
    <div id="prefForm">
        <form>
            <p>Make your choice </p>
            <select v-model="formData.siteId" v-on:change="fillFloors()">
                <option selected disabled hidden style='display: none' value=''></option>
                <option selected="selected" value="-1">--- Your site in th list  ---</option>
                <option v-for="site in optionSites" v-bind:key="site.id" v-bind:value="site.id">{{site.name}}</option>
            </select>

            <select v-model="formData.floorId" v-on:change="fillDesks()">
                <option >--- The floor in th list  --- </option>
                <option v-for="floor in floors" v-bind:key="floor.id" v-bind:value="floor.id">{{floor.id}}</option>
            </select>

            <select v-model="formData.deskId">
                <option>--- Your desk  --- </option>
                <option v-for="desk in desks" v-bind:key="desk.id" v-bind:value="desk.id">{{desk.desknumber}}</option>
            </select>

            <div class="ui blue button"  @click="registerPref()">
                <i class="heart icon" ></i>
            </div>

            <div class="ui blue button"   @click="closePref()">
                <i class="cancel icon" ></i>
            </div>

        </form>

    </div>
</template>

<script>
    export default {
        name: "MultiSearch"
    }
</script>

<style scoped>

</style>
