import packJson from "../../package.json"

const config = {
    env: process.env.NODE_ENV,
    appName: '智慧门诊疫苗接种应用系统',
    version: packJson.version 
};

export default config