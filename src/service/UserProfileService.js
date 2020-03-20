import localService from "./LocalStorageService";
import ipService from "./IpInfoService";

const USER_GEOLOC = "userGeoloc";
export class UserProfileService{

    async init(){

        let userIp = await localService.getFromLocal(USER_GEOLOC);

        if(!userIp || userIp===null){
            await ipService.getIpInfo().then(resp=>{
                userIp=resp.data;
            localService.saveToLocal(USER_GEOLOC,userIp);
            });

        }
    }


    async getUserIpInfo(){

        let userIp = await localService.getFromLocal(USER_GEOLOC);

        if(!userIp || userIp===null){
            await ipService.getIpInfo().then(resp=>{
                userIp=resp.data;
                if(userIp){
                localService.saveToLocal(USER_GEOLOC,userIp);
                }

            });

        }

        return userIp;
    }

}

export  default  new UserProfileService();