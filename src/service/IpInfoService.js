import axios from "axios";

const url = 'http://ipinfo.io/json';
const key ="d09b28f75870c9";

export class IpInfoService {

    getIpInfo(){
        return axios.get(`${url}?token=${key}`);
    }


}

export default new IpInfoService();