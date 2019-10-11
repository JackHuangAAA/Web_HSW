import packJson from '../../package.json'

let config = {
  env: process.env.NODE_ENV,
  appName: '银信博荣智慧疫苗接种柜',
  version: packJson.version,
  type: 1
}

export default config
