import { useNavigate } from 'react-router-dom';
import { Button, ButtonProps } from 'antd';
import { LeftOutlined } from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import { PATH_HOME } from '../../constants';

type Props = {
  wIcon?: boolean;
  iconOnly?: boolean;
} & ButtonProps;

export const BackBtn = ({ wIcon, iconOnly, ...others }: Props) => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <Button
      icon={wIcon || iconOnly ? <LeftOutlined /> : null}
      onClick={() => navigate(PATH_HOME.root)}
      {...others}
    >
      {!iconOnly && t('common.backHome')}
    </Button>
  );
};
