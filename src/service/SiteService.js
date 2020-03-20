import axios from 'axios';
import profile from './UserProfileService';
      //const FLEX_URL = `http://localhost:9090/flexoffice/sites`;
      //const FLEX_URL = `http://localhost:8080/flexoffice/sites`;
       const DOCKER_HOST=`flexo-backend`;
      const FLEX_URL = `http://${DOCKER_HOST}:8080/flexoffice/sites`;
       //const FLEX_URL = `http://localhost:8080/flexoffice/sites`;
       //const host = `192.168.1.85`;

    //const FLEX_URL = `http://${host}:8080/flexoffice/sites`;

    export const MAX_FAV = 5;



 class SiteService {

    constructor(){}



     loadImage(item){
         return axios({
             method: 'get',
             url: `${FLEX_URL}/${item.siteId}/floors/${item.floorId}/desks/${item.deskId}/qrcode`,
             responseType: 'blob'
         });
     }



    search(term){

        let tab = term.split();

        let key = {
            siteName:tab[0],
            floorName:tab[1]
        };
        return axios.post(`${FLEX_URL}/search`,key);
    }


    getSiteById(siteId){
        return axios.get(`${FLEX_URL}/${siteId}`);
    }

    getSites(){
        return axios.get(`${FLEX_URL}`);
    }

    getFloorsBySiteId(siteId){
        return axios.get(`${FLEX_URL}/${siteId}/floors`);
    }

    updateDesk(siteId, floorId, deskId, desk){
        return this.callRequest(`${FLEX_URL}/${siteId}/floors/${floorId}/desks/${deskId}`,'PUT',desk);
    }

    getDesk(siteId, floorId, deskId){
        return this.callRequest(`${FLEX_URL}/${siteId}/floors/${floorId}/desks/${deskId}`,'GET');
    }



    getDesksBySiteIddAndFid(siteId, floorId){
        return axios.get(`${FLEX_URL}/${siteId}/floors/${floorId}/desks`);

    }

    async callRequest(url,method,data, headers,responseType){
          let info = await profile.getUserIpInfo();

        let config = {
            headers: {
                userIp: info.ip,
            }
        }

           return axios.request({
            headers:config,
            url:url,
            method:method,
            data:data,
            responseType: responseType
        });
    }

    getQrCode(siteId, floorId, deskId) {
        let   url = `${FLEX_URL}/${siteId}/floors/${floorId}/desks/${deskId}/qrcode`;
        let config = {
            responseType: 'image/png'
        };
        return axios.get(url,config);
        
    }


    getCommendation(kpoint) {
        let   url = `${FLEX_URL}/recommendations`;
        return axios.post(url,kpoint);
    }

    getReportType() {
        let reports =[
            {
                "code":"01",
                "description" :"in use"
            }
        ];
        return reports;

}

    saveReport(report) {
        ///sites/{siteId}/floors/{floorId}/desks/{deskId}/report

        let   url = `${FLEX_URL}/${report.siteId}/floors/${report.floorId}/desks/${report.deskId}/report`;

       return this.callRequest(url,'PUT',report);

    }

    async getProfile() {
        let info = await profile.getUserIpInfo();
        let currentUser =info.ip;
        if (currentUser){
        let ipNormal = currentUser.split('.').join('-');
            let   url = `${FLEX_URL}/profile/${ipNormal}`;
        return this.callRequest(url,'GET');

        }
    }

    async getGlobalProfile() {
        let info = await profile.getUserIpInfo();
        let currentUser =info.ip;
        if (currentUser){
            let   url = `${FLEX_URL}/stats/`;
        return this.callRequest(url,'GET');

        }
    }

    async getCurrentDesk() {
        let currentDesk = null;
         let profile = await this.getProfile().data;
        if (profile){
            let idxCur = profile.sittings.findIndex(sit => sit.end === null && sit.start);

            if (idxCur!==-1){
                currentDesk = this.sittings[idxCur];
                console.log(`${JSON.stringify(currentDesk)}`);
            }
        }
        return currentDesk;

    }


        saveProfile(currentUser) {
        let ipNormal = currentUser.split('.').join('-')
        let   url = `${FLEX_URL}/profile/${ipNormal}`;
        return this.callRequest(url,'POST');
    }

    deleteFav(pref) {
        let   url = `${FLEX_URL}/${pref.siteId}/floors/${pref.floorId}/desks/${pref.deskId}/profile/${pref.userIp}/favorites`;
        return this.callRequest(url,'DELETE');
    }
}

export default  new SiteService();
