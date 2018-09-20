import React from 'react'
import PropTypes from 'prop-types'
import { request } from '../../utils/'
import { COOKIE } from 'utils'
import { Upload, Button, Icon, message, Tooltip } from 'antd'
const { clearCookie, keepCookie, getCookie } = COOKIE

class MyUpload extends React.Component {
  state = {
    uploadaction: '/upload/uploadFiles',
    delaction: '/upload/delFileByAttachFileId',
    queryaction: '/upload/queryFileByBatchId',
    downloadaction: '/upload/downloadFileById',
    fileList: [],
    defaultFileList: [],
    uploading: false,
    batchId: this.props.batchId ? this.props.batchId : '',
    Selectdisabled: this.props.Selectdisabled ? this.props.Selectdisabled : false,
    title: this.props.title ? this.props.title : '',
  }
  componentDidMount () {
    this.state.batchId !== '' ? this.handleRequest(this.state.batchId) : ''
  }
  onRemove = (file) => {
    if (this.state.Selectdisabled) {
      return
    }
    this.setState(({ fileList }) => {
      const index = fileList.indexOf(file)
      const newFileList = fileList.slice()
      if (this.state.batchId !== '') {
        const id = newFileList[index].uid
        request(this.state.queryaction, {
          body: JSON.stringify({
            id: this.state.batchId,
          }),
        }).then((json) => {
          if (json.success) {
            let jsonfileList = json.data.fileList
            for (let [index, elem] of jsonfileList.entries()) {
              if (elem.id === id) {
                this.onRemovedel(id)
                break
              }
            }
          }
        })
      }
      newFileList.splice(index, 1)
      return {
        fileList: newFileList,
      }
    })
  }
  onRemovedel = (id) => {
    request(this.state.delaction, {
      body: JSON.stringify({
        id,
      }),
    })
  }
  setfileList = () => {
    this.setState({
      defaultFileList: [],
      fileList: [],
    })
  }
  handleRequest = (batchId) => {
    request(this.state.queryaction, {
      body: JSON.stringify({
        id: batchId,
      }),
      showMsg: false,
    }).then((json) => {
      let defaultFileList = []
      if (json.success && json.data) {
        let jsonfileList = json.data.fileList
        for (let [index, elem] of jsonfileList.entries()) {
          let defaultfile = {
            uid: elem.id,
            name: elem.fileName,
            status: 'done',
            url: `${this.state.downloadaction}?id=${elem.id}`,
          }
          defaultFileList.push(defaultfile)
        }
        this.setState({
          batchId,
          defaultFileList,
          fileList: defaultFileList,
        })
      }
    })
  }
  beforeUpload = (file) => {
    if (file.name.length > 100) {
      message.error('文件名不能超过100个字符')
    } else {
      this.setState(({ fileList }) => ({
        fileList: [...fileList, file],
      }))
    }
    return false
  }
  customRequest = () => {
    request(this.state.uploadaction, {
      body: JSON.stringify({
        fileTypeId: '1',
        files: this.state.fileList,
        batchId: this.state.batchId,
      }),
    })
  }

  handleUpload = () => {
    const X_AUTH_TOKEN = getCookie('X-AUTH-TOKEN')
    const X_AUTH_USER = getCookie('X-AUTH-USER')
    const { fileList } = this.state
    let formData = new FormData()
    for (let [index, elem] of fileList.entries()) {
      formData.append(`files[${index}]`, elem)
    }
    formData.append('fileTypeId', '1')
    formData.append('batchId', this.state.batchId)
    this.setState({
      uploading: true,
    })
    fetch('/api/upload/uploadFiles', {
      method: 'post',
      headers: {
        'X-AUTH-TOKEN': X_AUTH_TOKEN,
        'X-AUTH-USER': X_AUTH_USER,
      },
      body: formData,
      credentials: 'include',
    }).then((response) => {
      return response.json()
    }).then((json) => {
      if (Number(json.code) === 200) {
        this.setState({
          uploading: false,
        })
        const BatchId = json.data.id
        this.props.setBatchId(BatchId)
        this.handleRequest(BatchId)
        message.success(json.msg)
      } else {
        this.setState({
          uploading: false,
        })
        message.error(json.msg)
      }
    })
  }
  clearList = () => {
    this.setState({
      fileList: [],
    })
  }
  render () {
    const { Selectdisabled, fileList, batchId } = this.state
    const Uploaddisabled = Selectdisabled ? 'true' : fileList.length === 0
    const props = {
      onRemove: this.onRemove,
      beforeUpload: this.beforeUpload,
      fileList,
      defaultFileList: this.state.defaultFileList,
      data: {
        fileTypeId: '1',
        files: fileList,
        batchId,
      },
    }
    return (
      <div>
        <Upload {...props} action={this.state.uploadaction} withCredentials multiple>
          {
            this.state.title ?
            (
              <Tooltip placement="top" title={this.state.title}>
                <Button
                  disabled={Selectdisabled}
                >
                  <Icon type="upload" /> 选择文件
                </Button>
              </Tooltip>
            ) : (
              <Button
                disabled={Selectdisabled}
              >
                <Icon type="upload" /> 选择文件
              </Button>
            )
          }
        </Upload>
        <Button
          className="upload-demo-start"
          type="primary"
          onClick={this.handleUpload}
          disabled={Uploaddisabled}
          loading={this.state.uploading}
        >
          {this.state.uploading ? '上传中' : '上传文件'}
        </Button>
      </div>
    )
  }
}

MyUpload.propTypes = {
  action: PropTypes.obj,
  Selectdisabled: PropTypes.bool,
  onRemove: PropTypes.func,
  setBatchId: PropTypes.func,
  batchId: PropTypes.obj,
  fileAdd: PropTypes.bool,
  title: PropTypes.string,
}

export default MyUpload
