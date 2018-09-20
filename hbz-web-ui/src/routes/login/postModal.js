import React from 'react'
import { Modal, Button, Icon } from 'antd'
import PropTypes from 'prop-types'
import styles from './postModal.less'

const postModal = ({ visible, posts, dispatch }) => {
  function selectPost (post) {
    dispatch({ type: 'login/handlePostModal', payload: post })
  }
  function handleCancel () {
    dispatch({ type: 'login/handlePostModal', payload: { visible: false } })
  }
  let postList = Array.from(posts).map(item => <Button className={styles.postItem} icon="user" onClick={selectPost.bind(this, item)}>{item.postName}</Button>)
  return (
    <Modal
      title={<div><Icon type="smile-o" /> 恭喜你，登录成功！请选择岗位</div>}
      wrapClassName="vertical-center-modal"
      visible={visible}
      onCancel={handleCancel.bind(this)}
      footer={null}
    >
      <div className={styles.postList}>
        {postList}
      </div>
    </Modal>
  )
}
postModal.propTypes = {
  visible: PropTypes.bool.isRequired,
  dispatch: PropTypes.func.isRequired,
  posts: PropTypes.isRequired,
}
export default postModal
