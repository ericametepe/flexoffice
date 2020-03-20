<template>
    <div id="header">
        <h4 class="bg-primary text-white text-center p-2">
            Welcome to Flex Office App
        </h4>
        <div class="ui secondary  menu">
            <router-link to="/">
            <a  class="active item">
                Home
            </a>
            </router-link>

            <router-link to="pref">
            <a class="item">
                Preferences
            </a>
            </router-link>

            <router-link to="admin">
            <a class="item">
                Activities
                <Notification></Notification>

            </a>
            </router-link>
            <div class="right menu">
                <div class="item">
                    <Search/>
                </div>

            </div>
        </div>
        <img class="ui centered small image" alt="Flex logo" src="../../assets/flexoffice-logo.jpg">
    </div>
    
</template>

<script>

    import Search from "./Search";
    import Notification from "../Notification";

    export default {
        name: "Header",
        components: {Notification, Search},
        data(){
        return{
        }
        }

    }


</script>

