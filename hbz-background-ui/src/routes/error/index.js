import React from 'react'
import { Icon } from 'antd'
import { Link } from 'react-router'
import styles from './index.less'

const Error = () => (
  <div className="content-inner">
    <div className={styles.error}>
      <Icon type="frown-o" />
      <h1>404 Not Found</h1>
      <p>抱歉，该页面不存在或您没有相应的访问权限</p>
      <h1>
        <Link to="/workbench">返回主页</Link>
      </h1>
    </div>
  </div>
)

export default Error
