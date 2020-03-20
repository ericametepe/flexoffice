<template>
    <div>

    <h4 class="ui horizontal divider header">
        <i class="bar chart icon"></i>
        Your behaviour in stats...
    </h4>
        <div class="ui four statistics">
            <div class="ui red statistic">
                <div class="value">
                    {{profile.statParameters.reportCounter}}
                </div>
                <div class="label">
                    Your Reports
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                    <span>{{ gprofile.reports.sum}}</span>
                </div>
                <div class="label">
                    Global report
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                    <span>{{ gprofile.reports.average}}</span>
                </div>
                <div class="label">
                    Global Report average
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                   <i class="icon user"></i>
                    {{gprofile.reports.count}}
                </div>
                <div class="label">
                    Total of Reporters
                </div>
            </div>
        </div>



        <div class="ui four statistics">
            <div class="statistic">
                <div class="value">
                    {{profile.statParameters.sittingCounter}}
                </div>
                <div class="label">
                    Your total use of desks
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                    <span>{{ gprofile.sittings.sum}}</span>
                </div>
                <div class="label">
                    Global sittings
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                    <span>{{ gprofile.sittings.average}}</span>
                </div>
                <div class="label">
                    Global sittings average
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                   <i class="icon user"></i>
                    {{gprofile.sittings.count}}
                </div>
                <div class="label">
                    Total of users
                </div>
            </div>
        </div>


        <div class="ui four statistics">
            <div class="statistic">
                <div class="value">
                    {{profile.statParameters.ratingCounter}}
                </div>
                <div class="label">
                    Ratings
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                    <span>{{ gprofile.ratingStats.sum}}</span>
                </div>
                <div class="label">
                    Global Ratings
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                    <span>{{ gprofile.ratingStats.average}}</span>
                </div>
                <div class="label">
                     Rating average
                </div>
            </div>
            <div class="statistic">
                <div class="value">
                   <i class="icon user"></i>
                    {{gprofile.ratingStats.count}}
                </div>
                <div class="label">
                    Total of Reviewers
                </div>
            </div>
        </div>


        <div class="ui four statistics">
            <div class="statistic">
                <div class="value">
                    {{profile.statParameters.ratingStars.average  | formatNumber}}
                </div>
                <div class="label">
                    Ratings stars average
                </div>
            </div>
            <div class="ui orange statistic">
                <div class="value">
                    <span>{{ profile.statParameters.ratingStars.min}}</span>
                </div>
                <div class="label">
                   Your Minimum rating stars
                </div>
            </div>
            <div class="ui purple statistic">
                <div class="value">
                    <span>{{ gprofile.ratingStarsStats.average | formatNumber}}</span>
                </div>
                <div class="label">
                     Reviewer's Rating average
                </div>
            </div>
            <div class="ui yellow statistic">
                <div class="value">
                   <i class="icon user"></i>
                    {{gprofile.ratingStats.count| formatNumber}}

                </div>
                <div class="label">
                    Total of Reviewers
                </div>
            </div>
        </div>

        <div>
            <pack-chart :tweet-data="chartData"></pack-chart>
        </div>

    </div>
    
</template>

<script>
    import siteService from "../service/SiteService";
    import PackChart from "./Chart";
    import * as d3 from "d3";


    export default {
        name: "Statistics",
        components: {PackChart},
        data(){
            return{
                chartData:[],
                profile : {"statParameters": {
                        "reportCounter": 0,
                "sittingCounter": 0,
                "ratingCounter": 0,
                "ratingStars": {
                "count": 0,
                    "sum": 0,
                    "min": 0,
                    "max": 0,
                    "average": 0
                  }
                  }},
                gprofile : {
                    "reports": {
                        "count": 0,
                        "sum": 0,
                        "min": 0,
                        "max": 0,
                        "average": 0
                    },
                    "sittings": {
                        "count": 0,
                        "sum": 0,
                        "min": 0,
                        "max": 0,
                        "average": 0
                    },
                    "favoris": {
                        "count": 0,
                        "sum": 0,
                        "min": 0,
                        "max": 0,
                        "average": 0
                    },
                    "ratingStats": {
                        "count": 0,
                        "sum": 0,
                        "min": 0,
                        "max": 0,
                        "average": 0
                    },
                    "ratingStarsStats": {
                        "count": 0,
                        "sum": 0,
                        "min": 0,
                        "max": 0,
                        "average": 0
                    }
                },
                ada_lovelace_url:null
            }
        },
        created(){
            siteService.getProfile()
                .then(resp => this.profile = resp.data);
            siteService.getGlobalProfile().then(gresp => this.gprofile=gresp.data);
        },
        mounted(){
          this.fetchData();
        },

        methods:{
            async fetchData() {
                let data = await d3.json("./tweets.json");
                this.chartData = data;
            }
        },


        filters:{
            formatNumber(value){
                return new Intl.NumberFormat('en-IN', { maximumSignificantDigits: 3}).format(value);
            }
        }
    }
</script>

<style scoped>

</style>
