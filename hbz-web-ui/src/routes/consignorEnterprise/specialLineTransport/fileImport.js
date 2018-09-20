import React from 'react'
import axios from 'axios'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Button, message, Upload } from 'antd'
import { COOKIE } from 'utils'
import styles from './index.less'

const { getCookie } = COOKIE

const FileImport = ({
  dispatch,
  specialLineTransport,
}) => {
  const { baseFileList, baseUploading, orderType } = specialLineTransport
  const uploadUrl = orderType === 'fslOrder' ? '/api/web/flsOrder/importHbzFslOrder' : '/api/web/ltlOrder/importHbzLtlOrder'
  const dowloadUrl = orderType === 'fslOrder' ? '/api/web/flsOrder/downloadHbzFslOrderTemplate ' : '/api/web/ltlOrder/downloadHbzLtlOrderTemplate'
  const X_AUTH_TOKEN = getCookie('X-AUTH-TOKEN')
  const X_AUTH_USER = getCookie('X-AUTH-USER')
  const titleObj = {
    fslOrder: '整车批量下载和导入',
    LtlOrder: '零担批量下载和导入',
  }
  const showText = {
    fslOrder: '整车批量导入模板.xls',
    LtlOrder: '零担批量导入模板.xls',
  }
  const importProp = {
    beforeUpload (file) {
      const isFileType = file.name.substr(file.name.lastIndexOf('.')).toLowerCase()
      if (isFileType !== '.xls') {
        message.error('只接受格式为.xls的文件！')
      } else {
        dispatch({ type: 'specialLineTransport/changeStates', payload: { baseFileList: [...baseFileList, file] } })
      }
      return false
    },
    onRemove (file) {
      const index = baseFileList.indexOf(file)
      const newFileList = baseFileList.slice()
      newFileList.splice(index, 1)
      dispatch({ type: 'specialLineTransport/changeStates', payload: { baseFileList: newFileList } })
    },
    fileList: baseFileList,
  }
  const baseUpload = () => {
    let formData = new FormData()
    for (let [index, elem] of baseFileList.entries()) {
      formData.append('file', elem)
    }
    dispatch({
      type: 'specialLineTransport/changeStates',
      payload: {
        baseUploading: true,
      },
    })
    axios.post(uploadUrl, formData, {
      // method: 'post',
      headers: {
        'X-AUTH-TOKEN': X_AUTH_TOKEN,
        'X-AUTH-USER': X_AUTH_USER,
      },
    }).then((json) => {
      if (Number(json.data.code) === 200) {
        message.success(json.data.msg)
        dispatch({
          type: 'specialLineTransport/changeStates',
          payload: {
            baseUploading: false,
            baseFileList: [],
          },
        })
      } else {
        message.error(json.data.msg)
        dispatch({
          type: 'specialLineTransport/changeStates',
          payload: {
            baseUploading: false,
          },
        })
      }
    })
  }
  const dowload = () => {
    axios.get(dowloadUrl, {
      headers: {
        'X-AUTH-TOKEN': X_AUTH_TOKEN,
        'X-AUTH-USER': X_AUTH_USER,
      },
      responseType: 'blob',
    }).then(res => {
      let url = window.URL.createObjectURL(res.data)
      let filename = res.headers['content-disposition'].split('=')[1]
      if (navigator.appVersion.toString().indexOf('.NET') > 0) {
        window.navigator.msSaveBlob(res.data, filename)
      } else {
        let a = document.getElementById('test')
        a.href = url
        a.download = filename
        a.click()
        window.URL.revokeObjectURL(url)
      }
    })
  }
  return (
    <div>
      <Card title={titleObj[orderType]}>
        <span className={styles.dowload}>{showText[orderType]}</span>
        <Button onClick={dowload}>下载</Button>
        <a target="_blank" id="test"></a>
        <div>
          <span>批量创建说明：</span>
          <span>批量创建是平台提供给企业使用的，通过excel模板填充数据后，将excel文件上传至系统，进行一次上传，创建多条数据的功能</span>
        </div>
        <div className={styles.upload}>
          <Upload {...importProp}>
            <Button disabled={baseFileList.length === 1}>
              <Icon type="upload" />选择文件
            </Button>
          </Upload>
          <Button
            loading={baseUploading}
            onClick={baseUpload}
            disabled={baseFileList.length > 0 ? false : 'true'}
          >
            {baseUploading ? '上传文件中' : '上传'}
          </Button>
        </div>
      </Card>
    </div>
  )
}

FileImport.propTypes = {
  form: PropTypes.isRequired,
  specialLineTransport: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
export default connect(({ specialLineTransport }) => ({ specialLineTransport }))(FileImport)
