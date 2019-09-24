import packJson from "../../package.json"

const config = {
    env: process.env.NODE_ENV,
    appName: '银信博荣智慧疫苗冷藏柜',
    version: packJson.version 
};

export default config