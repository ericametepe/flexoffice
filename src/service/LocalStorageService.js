 export class LocalStorageService{

    getFromLocal(key){
      return  JSON.parse(localStorage.getItem(key));

    }

    saveToLocal(key, data){
        localStorage.setItem(key,JSON.stringify(data));
    }

    mergeToLocal(key, newData){
        let load = this.getFromLocal(key);

        if(Array.isArray(load)){
            load.push(newData);
        }
        this.saveToLocal(key,load);
    }

}

export default new LocalStorageService();