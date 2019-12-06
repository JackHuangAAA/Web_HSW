import packJson from "../../package.json"

const config = {
    //env: process.env.NODE_ENV,
    env: 'development',
    appName: '银信博荣智慧疫苗物联平台',
    version: packJson.version 
};

export default config;