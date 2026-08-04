type Profile = 'Eficiente' | 'Moderado' | 'Ineficiente'

interface ProfileBadgeProps {
  profile: Profile
}

const profileClass: Record<Profile, string> = {
  Eficiente: 'profile-badge--eficiente',
  Moderado: 'profile-badge--moderado',
  Ineficiente: 'profile-badge--ineficiente',
}

export default function ProfileBadge({ profile }: ProfileBadgeProps) {
  return (
    <span className={`profile-badge ${profileClass[profile]}`}>
      {profile}
    </span>
  )
}
