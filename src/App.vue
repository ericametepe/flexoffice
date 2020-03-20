<template>
  <div id="app">
    <div class="container-fluid">
    <Header></Header>
      <div class="row">
        <div class="col m-2">
          <router-view></router-view>
        </div>
      </div>
    <Footer></Footer>
    </div>
  </div>
</template>

<script>
  import Header from "./components/layout/Header"
  import Footer from "./components/layout/Footer"
export default {
  name: 'app',
  components: {
      Header,
      Footer
  },
  created(){

  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
